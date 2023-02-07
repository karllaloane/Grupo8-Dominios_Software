package br.ufg.sep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufg.sep.entity.AnexoQuestao;

@Repository
public interface AnexoQuestaoRepository extends JpaRepository<AnexoQuestao,Long> {

}
