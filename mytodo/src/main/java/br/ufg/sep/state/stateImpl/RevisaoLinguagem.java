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
    public Boolean enviarParaBanca(Questao questao, Revisao revisao) {
        RevisaoBanca revisaoBanca = new RevisaoBanca();
        revisaoBanca.setRevisao(revisao);
        revisaoBanca.setCorrecao(this.correcao);
        questao.setState(revisaoBanca);
        return true;
    }

    public String toString(){
        return "Em Revisão Linguística";
    }
}
