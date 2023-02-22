package br.ufg.sep.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Map;

@Entity
public class Revisao extends AbstractEntity{

    @ElementCollection
    @JoinColumn(name="itens_analisados")
    @OnDelete(action = OnDeleteAction.CASCADE)//TESTAR
    Map<String,Atendimento> itemAnalisado;
    //  EXEMPLO: String chave: Paralelismo ;; String valor: true ( atende parcialmente)
    // true: atende, false: nao atende, null: atende parcialmente
    // ACEITO SUGESTÕES DE REFATORAMENTO

    String orientacoes;

    public Map<String, Atendimento> getItemAnalisado() {
        return itemAnalisado;
    }

    public void setItemAnalisado(Map<String, Atendimento> itemAnalisado) {
        this.itemAnalisado = itemAnalisado;
    }

    public String getOrientacoes() {
        return orientacoes;
    }

    public void setOrientacoes(String orientacoes) {
        this.orientacoes = orientacoes;
    }

    public Revisao(){

    }


}
