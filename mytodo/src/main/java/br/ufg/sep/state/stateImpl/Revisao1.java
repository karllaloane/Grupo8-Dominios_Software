package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.*;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;

import javax.persistence.Entity;

@Entity
public class Revisao1 extends QuestaoState {

    public Revisao1(){
        super();
    }

    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao corr) {// ENVIA PARA REVISAO

        Revisao2 revisao2 = new Revisao2();
        revisao2.setCorrecao(corr);
        questao.setState(revisao2);
        return true;
    }


    @Override
    public Boolean enviarParaBanca(Questao questao, Revisao rev1) {//envia de volta para banca pela primeira vez


        Correcao1 corr1State = new Correcao1(questao);
        corr1State.setRevisao(rev1);; //envia para banca com a revisão realizada
        questao.setState(corr1State); //muda o estado
        return true;
    }
    public String toString(){
        return "Em Revisão I";
    }
}
