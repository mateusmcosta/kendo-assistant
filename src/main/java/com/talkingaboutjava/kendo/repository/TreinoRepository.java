package com.talkingaboutjava.kendo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.talkingaboutjava.kendo.data.TreinosPresencaDTO;
import com.talkingaboutjava.kendo.entity.Treino;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long>{

    @Query("""
       SELECT new com.talkingaboutjava.kendo.data.TreinosPresencaDTO(
           t.data, 
           t.descricaoTreino, 
           COUNT(a.id)
       ) 
       FROM Treino t 
            JOIN t.alunos a 
        GROUP BY t.id, t.data, t.descricaoTreino
       """)
    List<TreinosPresencaDTO> findTreinosEPresencas();

    List<Treino> findByData(LocalDate data);

    List<Treino> findByDataBetween(LocalDate inicio, LocalDate fim);
}