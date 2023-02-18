package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;
import jdk.jshell.execution.Util;

import javax.persistence.Entity;

@Entity
public class Correcao2 extends QuestaoState {

    public Correcao2(Questao questaoAnterior){
        super();
        UtilQuestao.copiarQuestao(questaoAnterior,this.questaoAnterior);
    }

    public Correcao2(){

    }
    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao correcao) {
        Revisao3 revisao3 = new Revisao3();
        revisao3.setCorrecao(correcao);
        revisao3.setRevisao(this.revisao);
        revisao3.setQuestaoAnterior(this.questaoAnterior);
        questao.setState(revisao3);
        return true;
    }

    public String toString(){
        return "Em Correção II";
    }
}
