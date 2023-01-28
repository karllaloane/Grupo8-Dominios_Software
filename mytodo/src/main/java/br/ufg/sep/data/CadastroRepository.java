package br.ufg.sep.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.Cadastro;


@Repository
public interface CadastroRepository extends JpaRepository<Cadastro,Long>{

}
