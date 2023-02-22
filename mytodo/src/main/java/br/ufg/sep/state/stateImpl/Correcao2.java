package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.*;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;
import jdk.jshell.execution.Util;

import javax.persistence.Entity;

@Entity
public class Correcao2 extends QuestaoState {

    public Correcao2(Questao questao){
        super();
        this.questaoAnterior =
                questao.getProva().getTipo().equals(TipoProva.DISCUSSIVA) ?
                        new QuestaoDiscursiva() : new QuestaoObjetiva();

        UtilQuestao.copiarQuestao(questao,this.questaoAnterior);
        System.out.println("QUESTAO ANTERIOR CRIADAAAAAAAAAA");
    }

    public Correcao2(){

    }
    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao correcao) { //enviar p R3
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
