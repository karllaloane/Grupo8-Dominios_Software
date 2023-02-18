package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;

import javax.persistence.Entity;

@Entity
public class RevisaoLinguagem extends QuestaoState {

    public RevisaoLinguagem(){
        super();
    }

    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao correcao) {

        return true;
    }

    public String toString(){
        return "Em Revisão Linguística";
    }
}
