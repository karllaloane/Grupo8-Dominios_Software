package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.*;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.UtilQuestao;

import javax.persistence.Entity;

@Entity
public class RevisaoBanca extends QuestaoState {

    public RevisaoBanca(Questao questao){
        super();
        this.questaoAnterior =
                questao.getProva().getTipo().equals(TipoProva.DISCUSSIVA) ?
                        new QuestaoDiscursiva() : new QuestaoObjetiva();

        UtilQuestao.copiarQuestao(questao,this.questaoAnterior);
        System.out.println("QUESTAO ANTERIOR CRIADAAAAAAAAAA");
    }

    public RevisaoBanca(){}

    @Override
    public Boolean concluir(Questao questao) {
        Concluida concluida = new Concluida();
        concluida.setRevisao(this.revisao);
        concluida.setCorrecao(this.correcao);
        concluida.setQuestaoAnterior(this.questaoAnterior);
        questao.setState(concluida);
        return true;
    }

    @Override
    public String toString() {
        return "Em Revisão da Banca";
    }
}
