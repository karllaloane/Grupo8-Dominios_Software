package br.ufg.sep.entity;

import javax.persistence.Entity;

@Entity
public class QuestaoDiscursiva extends Questao {
	
	private String respostaEsperada;

	public String getRespostaEsperada() {
		return respostaEsperada;
	}

	public void setRespostaEsperada(String respostaEsperada) {
		this.respostaEsperada = respostaEsperada;
	}

}
