package br.ufg.sep.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="provas")
public class Prova extends AbstractEntity{
	
	@ManyToOne()
	@JoinColumn(name="concurso_id", nullable = false)
	private Concurso concurso;
	
	@Column
	private String areaConhecimento;
	
	@OneToMany(mappedBy="prova", fetch = FetchType.LAZY,cascade= CascadeType.ALL)
	private List<Questao> questoes;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="responsavel_id", nullable = false)
	private Cadastro responsavel;

	@Column
	private String nivel;
	
	@Column
	private String descricao;

	public Concurso getConcurso() {
		return concurso;
	}

	public void setConcurso(Concurso concurso) {
		this.concurso = concurso;
	}

	public int getNumeroQuestoes() {
		return numeroQuestoes;
	}

	public void setNumeroQuestoes(int numeroQuestoes) {
		this.numeroQuestoes = numeroQuestoes;
	}

	@Column
	private int numeroQuestoes;

	public String getAreaConhecimento() {
		return areaConhecimento;
	}




	public void setAreaConhecimento(String areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}




	public List<Questao> getQuestoes() {
		return questoes;
	}




	public void setQuestoes(List<Questao> questoes) {
		this.questoes = questoes;
	}




	public Cadastro getResponsavel() {
		return responsavel;
	}




	public void setResponsavel(Cadastro responsavel) {
		this.responsavel = responsavel;
	}




	public String getNivel() {
		return nivel;
	}




	public void setNivel(String nivel) {
		this.nivel = nivel;
	}




	public String getDescricao() {
		return descricao;
	}




	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	public Prova(){
		
	}
	

	
}
