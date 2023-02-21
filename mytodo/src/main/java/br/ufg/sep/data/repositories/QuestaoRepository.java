package br.ufg.sep.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long>{

	List<Questao> findByProva(Prova prova);




	//Questao findById(Long id);
}
