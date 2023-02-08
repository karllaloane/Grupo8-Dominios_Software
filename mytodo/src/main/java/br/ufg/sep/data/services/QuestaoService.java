package br.ufg.sep.data.services;

import org.springframework.stereotype.Service;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.data.repositories.QuestaoRepository;

@Service
public class QuestaoService {
	
	public final QuestaoRepository repository;
	
    public QuestaoService(QuestaoRepository provaRepository){
        this.repository = provaRepository;
    }

    public QuestaoRepository getRepository() {
        return repository;
    }
}
