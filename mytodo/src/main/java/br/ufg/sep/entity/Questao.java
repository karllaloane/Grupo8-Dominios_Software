package br.ufg.sep.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Questao extends AbstractEntity {

	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="prova_id", nullable = false)
	private Prova prova;
	
	public Prova getProva() {
		return prova;
	}

	public void setProva(Prova prova) {
		this.prova = prova;
	}

	private int numeroQuestao;

	private String numeroCaracteres;
	public Questao(){
		
		
	}
	
	public Questao(int num) {
		this.numeroQuestao = num;
	}
	


	public String getNumeroCaracteres() {
		return numeroCaracteres;
	}

	public void setNumeroCaracteres(String numeroCaracteres) {
		this.numeroCaracteres = numeroCaracteres;
	}


	public int getNumeroQuestao() {
		return numeroQuestao;
	}

	public void setNumeroQuestao(int numeroQuestao) {
		this.numeroQuestao = numeroQuestao;
	}
	
	

}
