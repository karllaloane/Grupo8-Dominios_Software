package br.ufg.sep.state;

import br.ufg.sep.entity.AbstractEntity;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.stateImpl.Revisao1;

import javax.persistence.*;
import java.util.List;

@Entity
public abstract  class QuestaoState extends AbstractEntity implements  Elaboravel{

    @OneToOne(cascade = CascadeType.ALL)
    protected Questao questaoAnterior;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="ultima_revisao_id")
    protected Revisao revisao;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="ultima_correcao_id")
    protected Correcao correcao;


    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao correcao) { //enviar para REVISAO 1
        System.out.println("Nada a se fazer");
        return false; // false se não tiver nada a se fazer
    }

    @Override
    public Boolean enviarParaBanca(Questao questao, Revisao revisao) {
        System.out.println("Nada a se fazer");
        return false; // false se não tiver nada a se fazer

    }

    public Questao getQuestaoAnterior() {
        return questaoAnterior;
    }

    public void setQuestaoAnterior(Questao questaoAnterior) {
        this.questaoAnterior = questaoAnterior;
    }

    @Override
    public Boolean enviarParaRevisaoLinguagem(Questao questao, Revisao revisao) {
        return false;
    }

    @Override
    public Boolean concluir(Questao questao) {
        System.out.println("Nada a se fazer");
        return false; // false se não tiver nada a se fazer

    }

    @Override
    public Boolean guardarNoBanco(Questao questao) {
        System.out.println("Nada a se fazer");
        return false; // false se não tiver nada a se fazer

    }

    @Override
    public Boolean descartar(Questao questao, Revisao revisao) {
        System.out.println("Nada a se fazer");
        return false; // false se não tiver nada a se fazer

    }
/*********GETTERS AND SETTERS*********/


    public QuestaoState(){

    }

    public Revisao getRevisao() {
        return revisao;
    }

    public void setRevisao(Revisao revisao) {
        this.revisao = revisao;
    }

    public Correcao getCorrecao() {
        return correcao;
    }

    public void setCorrecao(Correcao correcao) {
        this.correcao = correcao;
    }


}
