package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.*;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;

import javax.persistence.Entity;

@Entity
public class Correcao1 extends QuestaoState {

    public Correcao1(Questao questao){
        super();
        this.questaoAnterior =
                questao.getProva().getTipo().equals(TipoProva.DISCUSSIVA) ?
                        new QuestaoDiscursiva() : new QuestaoObjetiva();
        UtilQuestao.copiarQuestao(questao,this.questaoAnterior);
        System.out.println("QUESTAO ANTERIOR CRIADAAAAAAAAAA");
    }

    public Correcao1(){
    }

    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao corr) {// enviar para R2
        Revisao2 revisao2 = new Revisao2();
        revisao2.setCorrecao(corr); // coloco a correção 1 recem feita
        revisao2.setRevisao(this.revisao);// coloco a primeira revisão (ultima revisao feita)
        revisao2.setQuestaoAnterior(this.questaoAnterior);//questao anterior: após a elaboração
        questao.setState(revisao2);// mudo o estado para em Revisao II
        return true;
    }

    @Override
    public String toString() {
        return "Em Correção I";
    }
}
