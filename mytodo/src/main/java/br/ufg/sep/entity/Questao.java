package br.ufg.sep.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Questao extends AbstractEntity {

	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="prova_id", nullable = false)
	private Prova prova;
	
	private int idQuestao;

	// motivo?
	private String numeroCaracteres;
	
	private String enunciado;
	
	private String conteudoEspecifico;
	
	private int nivelDificuldade;
	
	private String descricao;
	
	private String justificativa;
	
	@OneToOne(cascade = CascadeType.ALL)
	private AnexoQuestao anexo;
	
	
	public Questao(){
		
		
	}
	
	public Questao(int num) {
		this.idQuestao = num;
	}
	
	
	
	
	
	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getConteudoEspecifico() {
		return conteudoEspecifico;
	}

	public void setConteudoEspecifico(String conteudoEspecifico) {
		this.conteudoEspecifico = conteudoEspecifico;
	}

	public int getNivelDificuldade() {
		return nivelDificuldade;
	}

	public void setNivelDificuldade(int nivelDificuldade) {
		this.nivelDificuldade = nivelDificuldade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public AnexoQuestao getAnexo() {
		return anexo;
	}

	public void setAnexo(AnexoQuestao anexo) {
		this.anexo = anexo;
	}
	
	public Prova getProva() {
		return prova;
	}

	public void setProva(Prova prova) {
		this.prova = prova;
	}


	public String getNumeroCaracteres() {
		return numeroCaracteres;
	}

	public void setNumeroCaracteres(String numeroCaracteres) {
		this.numeroCaracteres = numeroCaracteres;
	}


	public int getIdQuestao() {
		return idQuestao;
	}

	public void setIdQuestao(int idQuestao) {
		this.idQuestao = idQuestao;
	}
	
	

}
