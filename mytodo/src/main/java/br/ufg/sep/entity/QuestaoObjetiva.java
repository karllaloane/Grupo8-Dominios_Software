package br.ufg.sep.entity;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class QuestaoObjetiva extends Questao {
	
	@ElementCollection
	private Map<String, Boolean> alternativas;

	public Map<String, Boolean> getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(Map<String, Boolean> alternativas) {
		this.alternativas = alternativas;
	}



}
