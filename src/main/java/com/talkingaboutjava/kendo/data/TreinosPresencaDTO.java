package com.talkingaboutjava.kendo.data;

import java.time.LocalDate;

import dev.langchain4j.model.output.structured.Description;

public record TreinosPresencaDTO(
    @Description("Data de quando o treino aconteceu.")
    LocalDate data, 
    
    @Description("Informações sobre o conteúdo do treino.")
    String treino, 

    @Description("Número total de Alunos presentes")
    Long presencas) {
}