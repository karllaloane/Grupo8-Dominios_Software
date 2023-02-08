package br.ufg.sep.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Prova;

@Repository
public interface ProvaRepository extends JpaRepository<Prova,Long> {
	
	List<Prova> findByElaborador(Cadastro idElaborador);

}
