package br.ufg.sep.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;

@Repository
public interface ConcursoRepository extends JpaRepository<Concurso,Long> {
	
	List<Concurso> findByNome(String nome);
}
