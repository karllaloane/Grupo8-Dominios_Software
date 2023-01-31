package br.ufg.sep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Questao;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long>{

	
}
