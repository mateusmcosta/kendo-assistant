package com.talkingaboutjava.kendo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.talkingaboutjava.kendo.data.AlunoDTO;
import com.talkingaboutjava.kendo.data.TreinoDTO;
import com.talkingaboutjava.kendo.entity.Aluno;
import com.talkingaboutjava.kendo.repository.AlunoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlunoService {

    private AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoDTO buscarPorZekken(String zekken) {
        log.info(">> BUSCANDO TODOS ALUNOS -> Zekken: "+zekken);
        return alunoRepository.findByNomeZekkenIgnoreCase(zekken.trim())
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Zekken '" + zekken + "' não encontrado nos registros."));
    }

    public AlunoDTO buscarPorNome(String nome) {
        return alunoRepository.findByNomeIgnoreCase(nome)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Aluno '" + nome + "' não encontrado nos registros."));
    }

    public List<AlunoDTO> buscarPorGraduacao(String graduacao) {
        return alunoRepository.findByGraduacao(graduacao)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AlunoDTO> buscarTodos() {
        log.info(">> BUSCANDO TODOS ALUNOS");
        return alunoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private AlunoDTO toDTO(Aluno a) {
        List<TreinoDTO> treinos = new ArrayList<>();
        
        if(a.getTreinos() != null){
            a.getTreinos().forEach(t -> treinos.add(new TreinoDTO(t.getData(), t.getDescricaoTreino())));
        }
        
        return new AlunoDTO(
            a.getNome(),
            a.getNomeZekken(),
            a.getGraduacao(),
            a.getDataCadastro(),
            treinos
        );
    }
}