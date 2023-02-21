package br.ufg.sep.entity;

import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

@Entity
public class QuestaoObjetiva extends Questao {
	
	@ElementCollection()
	private List<String> alternativas;

	private int quantAlternativas;
	
	private int alternativaCorreta;

	public int getQuantAlternativas() {
		return quantAlternativas;
	}

	public void setQuantAlternativas(int quantAlternativas) {
		this.quantAlternativas = quantAlternativas;
	}

	public int getAlternativaCorreta() {
		return alternativaCorreta;
	}

	public void setAlternativaCorreta(int alternativaCorreta) {
		this.alternativaCorreta = alternativaCorreta;
	}

	public List<String> getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(List<String> alternativas) {
		this.alternativas = alternativas;
	}


}
