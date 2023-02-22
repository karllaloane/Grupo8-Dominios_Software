package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.*;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;

import javax.persistence.Entity;

@Entity
public class RevisaoLinguagem extends QuestaoState {

    public RevisaoLinguagem(Questao questao){
        super();
        this.questaoAnterior =
                questao.getProva().getTipo().equals(TipoProva.DISCUSSIVA) ?
                        new QuestaoDiscursiva() : new QuestaoObjetiva();

        UtilQuestao.copiarQuestao(questao,this.questaoAnterior);
        System.out.println("QUESTAO ANTERIOR CRIADAAAAAAAAAA");
    }

    public RevisaoLinguagem(){}


    @Override
    public Boolean enviarParaBanca(Questao questao, Revisao revisao) {
        RevisaoBanca revisaoBanca = new RevisaoBanca(questao);
        revisaoBanca.setRevisao(revisao);
        revisaoBanca.setCorrecao(this.correcao);
        questao.setState(revisaoBanca);
        return true;
    }

    public String toString(){
        return "Em Revisão Linguística";
    }
}
