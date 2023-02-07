package br.ufg.sep.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Redacao extends Questao {
	
	@ElementCollection
	private List<String> espelhoCorrecao;

	public List<String> getEspelhoCorrecao() {
		return espelhoCorrecao;
	}

	public void setEspelhoCorrecao(List<String> espelhoCorrecao) {
		this.espelhoCorrecao = espelhoCorrecao;
	}



}
