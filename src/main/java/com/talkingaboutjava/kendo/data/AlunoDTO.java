package com.talkingaboutjava.kendo.data;

import java.time.LocalDate;

import java.util.List;

import dev.langchain4j.model.output.structured.Description;

public record AlunoDTO(

    @Description("Nome completo do praticante. Ex Luís Silva") String nome,
    @Description("Nome de guerra/Zekken. Ex: M.COSTA, MATEUS") String nomeZekken,
    @Description("Nível (Dan ou Kyu). Use para definir quem é o mais graduado.") String graduacao,
    @Description("Data de início no Dojo. Use para saber quem treina a mais tempo.")LocalDate dataCadastro,
    @Description("Lista de treinos que o aluno foi. Use para saber as presenças do aluno.") List<TreinoDTO> treinos ) {
}