package com.talkingaboutjava.kendo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talkingaboutjava.kendo.entity.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> { 

    Optional<Aluno> findByNomeZekkenIgnoreCase(String nomeZekken);

    Optional<Aluno> findByNomeIgnoreCase(String nome);

    List<Aluno> findByGraduacao(String graduacao);

    List<Aluno> findByNomeZekkenContainingIgnoreCase(String trechoZekken);
}
