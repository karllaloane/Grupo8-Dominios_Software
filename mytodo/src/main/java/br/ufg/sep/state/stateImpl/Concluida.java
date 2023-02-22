package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;

import javax.persistence.Entity;

@Entity
public class Concluida extends QuestaoState {

    public Concluida(){
        super();
    }

    @Override
    public Boolean guardarNoBanco(Questao questao) {
        Guardada guardadaState = new Guardada();
        guardadaState.setCorrecao(this.correcao);
        guardadaState.setRevisao(this.revisao);
        guardadaState.setQuestaoAnterior(this.questaoAnterior);
        questao.setState(guardadaState);
        return true;
    }

    @Override
    public String toString() {
        return "Concluída";
    }
}
