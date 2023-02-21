package br.ufg.sep.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Prova;

@Repository
public interface ProvaRepository extends JpaRepository<Prova,Long> {
	
	List<Prova> findByElaborador(Cadastro idElaborador);

	List<Prova> findByRevisor1(Cadastro revisor1);
	List<Prova> findByRevisor2(Cadastro revisor1);
	List<Prova> findByRevisor3(Cadastro revisor1);
	List<Prova> findByRevisorLinguagem(Cadastro revisor1);

	List<Prova> findByRevisor1OrRevisor2OrRevisor3OrRevisorLinguagem(
			Cadastro revisor1,
			Cadastro revisor2,
			Cadastro revisor3,
			Cadastro revisorLinguagem
	);
}
