package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;

import javax.persistence.Entity;

@Entity
public class Correcao1 extends QuestaoState {

    public Correcao1(Questao questaoAnterior){
        super();
        UtilQuestao.copiarQuestao(questaoAnterior,this.questaoAnterior);
        //elas serao iguais até q a correção seja finalizada
    }

    public Correcao1(){
    }

    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao corr1) {// enviar para R2
        Revisao2 revisao2 = new Revisao2();
        revisao2.setCorrecao(corr1); // coloco a correção 1 recem feita
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
