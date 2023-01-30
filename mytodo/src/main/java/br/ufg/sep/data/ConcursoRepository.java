package br.ufg.sep.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Concurso;

@Repository
public interface ConcursoRepository extends JpaRepository<Concurso,Long> {
	
	

}
