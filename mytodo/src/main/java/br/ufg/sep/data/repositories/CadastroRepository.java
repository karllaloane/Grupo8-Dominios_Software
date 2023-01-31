package br.ufg.sep.data.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Cadastro;


@Repository
public interface CadastroRepository extends JpaRepository<Cadastro,Long>{

	Cadastro findByCpf(String cpf);
	
	List<Cadastro> findByNome(String nome);

	

	
	
}
