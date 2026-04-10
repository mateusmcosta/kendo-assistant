package com.talkingaboutjava.kendo.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import com.talkingaboutjava.kendo.assistant.KendoAssistant;
import com.talkingaboutjava.kendo.tools.KendoTools;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AssistantConfig {

    private static final int WAIT_TIME = 60000;
    private static final String KENDO_VECTORS_JSON = "kendo_vectors.json";
    private static final Path VECTOR_STORE_PATH = Paths.get(KENDO_VECTORS_JSON);

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        
        if (Files.exists(VECTOR_STORE_PATH)) {

            log.info("### Carregando vetores do arquivo local... (Economizando Cota) ###");
            return InMemoryEmbeddingStore.fromFile(VECTOR_STORE_PATH);
        }

        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public ContentRetriever contentRetriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel) throws IOException {

        if (!Files.exists(VECTOR_STORE_PATH)) {
            log.info("### Arquivo de vetores não encontrado. Iniciando Ingestão de PDFs... ###");

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(1500, 200))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            getDocuments().forEach(doc -> indexFile(ingestor, doc));        
       
            ((InMemoryEmbeddingStore<TextSegment>) embeddingStore).serializeToFile(VECTOR_STORE_PATH);
            log.info("### Vetores salvos com sucesso em: " + VECTOR_STORE_PATH.toAbsolutePath() + " ###");
        }

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .minScore(0.6)
                .build();
    }

    @Bean
    public KendoAssistant assistant(ChatModel model, KendoTools kendoTools,
            ContentRetriever contentRetriever) {
        return AiServices.builder(KendoAssistant.class)
                .chatModel(model)
                .tools(kendoTools)
                .contentRetriever(contentRetriever)
                .build();
    }

    private List<Document> getDocuments() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:fontes/*.pdf");

        List<Document> documents = new ArrayList<>();
        ApachePdfBoxDocumentParser pdfParser = new ApachePdfBoxDocumentParser();

        for (Resource resource : resources) {
            readFile(documents, pdfParser, resource);
        }
        
        return documents;
    }

    private void readFile(List<Document> documents, ApachePdfBoxDocumentParser pdfParser, Resource resource)
            throws IOException {
        Path tempFile = Files.createTempFile("rag-kendo-", ".pdf");
        try (InputStream is = resource.getInputStream()) {
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            Document doc = FileSystemDocumentLoader.loadDocument(tempFile, pdfParser);
            documents.add(doc);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private void indexFile(EmbeddingStoreIngestor ingestor, Document doc) {
        log.info("Indexando PDF: " + doc.metadata().getString("file_name"));
        ingestor.ingest(doc);
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}
