package br.ufg.sep.data.services;

import br.ufg.sep.data.repositories.QuestaoStateRepository;
import br.ufg.sep.entity.Questao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufg.sep.data.repositories.QuestaoRepository;

@Service
public class QuestaoService {
	
	public final QuestaoRepository questaoRepository;
	public final QuestaoStateRepository questaoStateRepository;
    @Autowired
    public QuestaoService(QuestaoRepository questaoRepository,QuestaoStateRepository questaoStateRepository){
        this.questaoRepository = questaoRepository;
        this.questaoStateRepository = questaoStateRepository;
    }

    public void salvarEnvio(Questao questao){
        Questao antiga = questaoRepository.findById(questao.getId()).get();
        Long stateApagarId = Long.valueOf(antiga.getState().getId());
        questaoRepository.save(questao);
    };

    public QuestaoRepository getRepository() {
        return questaoRepository;
    }
}
