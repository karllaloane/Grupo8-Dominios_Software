package br.ufg.sep.entity;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Map;

@Entity
public class Correcao extends AbstractEntity{

    private AtendimentoSugestoes atendimentoSugestoes;
    //troquei para enum
    // feito com referencia ao arquivo-referencia:SISTEMA ELABORACAO DE PROVAS.pdf
    //0 para nao atendimento, 1 para atendimento parcial, 2 para atendimento total
    // aceito refatorações (enum, classe, string, sla)

    String justificativa;


    public Correcao(){

    }

    public AtendimentoSugestoes getAtendimentoSugestoes() {
        return atendimentoSugestoes;
    }

    public void setAtendimentoSugestoes(AtendimentoSugestoes atendimentoSugestoes) {
        this.atendimentoSugestoes = atendimentoSugestoes;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
