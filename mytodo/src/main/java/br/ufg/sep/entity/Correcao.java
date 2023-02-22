package br.ufg.sep.entity;

import javax.persistence.Entity;

@Entity
public class Correcao extends AbstractEntity{

    private Atendimento atendimentoSugestoes;
    //troquei para enum
    // feito com referencia ao arquivo-referencia:SISTEMA ELABORACAO DE PROVAS.pdf
    //0 para nao atendimento, 1 para atendimento parcial, 2 para atendimento total
    // aceito refatorações (enum, classe, string, sla)

    String justificativaDoAtendimento;


    public Correcao(){

    }

    public Atendimento getAtendimentoSugestoes() {
        return atendimentoSugestoes;
    }

    public void setAtendimentoSugestoes(Atendimento atendimentoSugestoes) {
        this.atendimentoSugestoes = atendimentoSugestoes;
    }

    public String getJustificativaDoAtendimento() {
        return justificativaDoAtendimento;
    }

    public void setJustificativaDoAtendimento(String justificativaDoAtendimento) {
        this.justificativaDoAtendimento = justificativaDoAtendimento;
    }
}
