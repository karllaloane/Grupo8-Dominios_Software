package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;
import org.springframework.context.annotation.Bean;

import javax.persistence.Entity;

@Entity
public class Revisao2 extends QuestaoState {

    public Revisao2(){
        super();
    }

    @Override
    public Boolean enviarParaRevisaoLinguagem(Questao questao, Revisao rev2) { // Envia para revisao Linguagem

        RevisaoLinguagem revisaoLinguagem = new RevisaoLinguagem(questao);
        revisaoLinguagem.setCorrecao(this.correcao); // correcao pd ser nula ou não
        revisaoLinguagem.setRevisao(rev2); // atualizo para a ultima revisao feita ( no caso a recém segunda)
        questao.setState(revisaoLinguagem);
        return true; // sinal que a operação foi um sucesso
    }



    @Override
    public Boolean enviarParaBanca(Questao questao, Revisao rev2) { // enviar para Correcao II

        Correcao2 corr2State = new Correcao2(questao);
        corr2State.setCorrecao(this.correcao); // mantenho a ultima correcao feita (pode ser nula ou nao)
        corr2State.setRevisao(rev2); // atualizo a revisao, mandando a ultima revisao feita, no caso a 2.
        questao.setState(corr2State);
        return true;// sinal que a operação foi um sucesso
    }

    @Override
    public String toString() {
        return "Em Revisão II";

    }
}
