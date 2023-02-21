package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;

import javax.persistence.Entity;

@Entity
public class Descartada extends QuestaoState {

    public Descartada(){
        super();
    }


    @Override
    public String toString() {
        return "Descartada";
    }
}
