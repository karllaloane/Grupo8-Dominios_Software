package br.ufg.sep.data.repositories;

import br.ufg.sep.state.QuestaoState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoStateRepository extends JpaRepository<QuestaoState,Long> {



}
