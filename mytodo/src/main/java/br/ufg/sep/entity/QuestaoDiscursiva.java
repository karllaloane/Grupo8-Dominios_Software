package br.ufg.sep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class QuestaoDiscursiva extends Questao {

	public QuestaoDiscursiva(){
		super();
	}
	
	@Column(length = 2054)
	private String respostaEsperada;

	public String getRespostaEsperada() {
		return respostaEsperada;
	}

	public void setRespostaEsperada(String respostaEsperada) {
		this.respostaEsperada = respostaEsperada;
	}

}
