package com.talkingaboutjava.kendo.tools;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.talkingaboutjava.kendo.data.AlunoDTO;
import com.talkingaboutjava.kendo.data.TreinoDTO;
import com.talkingaboutjava.kendo.data.TreinosPresencaDTO;
import com.talkingaboutjava.kendo.service.AlunoService;
import com.talkingaboutjava.kendo.service.TreinoService;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KendoTools {

    private TreinoService treinoService;
    private AlunoService alunoService;

    public KendoTools(TreinoService treinoService, AlunoService alunoService) {
        this.treinoService = treinoService;
        this.alunoService = alunoService;
    }

    @Tool("Lista todos os alunos cadastrados no Dojo para visão geral e comparação de graduações. Use essa tool também qual quiser saber qual o aluno mais vai a treinos")
    public List<AlunoDTO> getAlunos() {
       return alunoService.buscarTodos();
    }

    @Tool("Busca detalhes de um praticante específico pelo seu nome de guerra (Zekken)")
    public AlunoDTO infoAluno(@P("Zekken do aluno") String zekken) {
        return alunoService.buscarPorZekken(zekken);
    }

    @Tool("Busca treinos ocorridos em um período específico")
    public List<TreinoDTO> buscaTreinoPorPeriodo(
            @P("Data inicial no formato yyyy-MM-dd") String dataInicial,
            @P("Data final no formato yyyy-MM-dd") String dataFinal) {
        return treinoService.buscaPorPeriodo(LocalDate.parse(dataInicial), LocalDate.parse(dataFinal));
    }

    @Tool("Busca o histórico de treinos e quantidade de presenças.")
    public List<TreinosPresencaDTO> findTreinosEPresencas() {
        return treinoService.buscaPresencas();
    }


    @Tool("Busca a data atual. Use essa tool para obter a data corrente.")
    public LocalDate now(){
        return LocalDate.now();
    }
}
