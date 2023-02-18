package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Questao;
import br.ufg.sep.state.QuestaoState;

import javax.persistence.Entity;

@Entity
public class Correcao1 extends QuestaoState {

    public Correcao1(){
        super();
    }


    @Override
    public void enviarParaRevisao(Questao questao) {

    }

    @Override
    public void enviarParaCorrecao(Questao questao) {

    }

    @Override
    public void concluir(Questao questao) {

    }

    @Override
    public void guardar(Questao questao) {

    }

    @Override
    public void descartar(Questao questao) {

    }
}
