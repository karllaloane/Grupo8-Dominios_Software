package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;

import javax.persistence.Entity;

@Entity
public class Revisao3 extends QuestaoState {

    public Revisao3(){
        super();
    }

    @Override
    public Boolean enviarParaRevisaoLinguagem(Questao questao, Revisao revisao) {
        RevisaoLinguagem revisaoLinguagem = new RevisaoLinguagem(questao);
        revisaoLinguagem.setCorrecao(this.correcao);// correção feita
        revisaoLinguagem.setRevisao(revisao);// revisao da correção feita
        questao.setState(revisaoLinguagem);
        return true;
    }

    @Override
    public Boolean descartar(Questao questao, Revisao revisao) {
        Descartada descartada = new Descartada();
        descartada.setQuestaoAnterior(this.questaoAnterior);
        descartada.setCorrecao(this.correcao);
        descartada.setRevisao(this.revisao);
        questao.setState(descartada);
        return true;
    }

    @Override
    public String toString() {
        return "Em Revisão III";
    }
}
