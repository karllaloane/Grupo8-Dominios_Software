package br.ufg.sep.data.services;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.entity.Prova;
import org.springframework.stereotype.Service;

@Service
public class ProvaService {
    public final ProvaRepository repository;
    public ProvaService(ProvaRepository provaRepository){
        this.repository = provaRepository;
    }

    public void save(Prova prova) throws NullPointerException{
        if(prova.getElaborador()==null){
            throw new NullPointerException("Prova sem responsável");
        }
        if(prova.getConcurso()==null){
            throw  new NullPointerException("Prova sem concurso");
        }
        this.repository.save(prova);

    }

    public void update(Prova prova) throws NullPointerException{
        if(prova.getElaborador()==null){
            throw new NullPointerException("Prova sem responsável");
        }
        if(this.repository.findById(prova.getId())==null){
            throw new NullPointerException("Prova inexistente para realizar Update");
        }
        this.repository.save(prova);
    }

    public ProvaRepository getRepository() {
        return repository;
    }
}
