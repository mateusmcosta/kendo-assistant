package com.talkingaboutjava.kendo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.talkingaboutjava.kendo.data.TreinoDTO;
import com.talkingaboutjava.kendo.data.TreinosPresencaDTO;
import com.talkingaboutjava.kendo.repository.TreinoRepository;

@Service
public class TreinoService {

    private TreinoRepository treinoRepository;

    public TreinoService(TreinoRepository treinoRepository) {
        this.treinoRepository = treinoRepository;
    }

    public List<TreinoDTO> buscaTodos() {
        return treinoRepository.findAll()
                .stream()
                .map(t -> new TreinoDTO(t.getData(), t.getDescricaoTreino()))
                .toList();
    }

    public List<TreinoDTO> buscaPorData(LocalDate data) {
        return treinoRepository.findByData(data)
                .stream()
                .map(t -> new TreinoDTO(t.getData(), t.getDescricaoTreino()))
                .toList();
    }

    public List<TreinoDTO> buscaPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return treinoRepository.findByDataBetween(dataInicial, dataFinal)
                .stream()
                .map(t -> new TreinoDTO(t.getData(), t.getDescricaoTreino()))
                .toList();
    }

    public List<TreinosPresencaDTO> buscaPresencas() {
        return treinoRepository.findTreinosEPresencas();
    }
}
