package br.ufg.sep.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="provas")
public class Prova extends AbstractEntity{


	@ManyToOne()
	@JoinColumn(name="concurso_id", nullable = false)
	private Concurso concurso;

	@OneToMany(mappedBy="prova", fetch = FetchType.LAZY,cascade= CascadeType.ALL)
	@Column(nullable = true)
	private List<Questao> questoes;
	
	@ManyToOne()
	@JoinColumn(name="elaborador_id", nullable = false)
	private Cadastro elaborador;
	@ManyToOne()
	@JoinColumn(name="revisor1_id", nullable = false)
	private Cadastro revisor1;
	@ManyToOne()
	@JoinColumn(name="revisor2_id", nullable = false)
	private Cadastro revisor2;
	@ManyToOne()
	@JoinColumn(name="revisor3_id", nullable = false)
	private Cadastro revisor3;

	@ManyToOne()
	@JoinColumn(name="revisor_linguagem_id", nullable = false)
	private Cadastro revisorLinguagem;

	@Column(nullable = true)
	private String areaConhecimento;

	@Enumerated(EnumType.STRING)
	private NivelProva nivel;

	@Enumerated(EnumType.STRING)
	private TipoProva tipo;
	
	@Column(nullable = true)
	private String descricao;

	@Column(nullable = true)
	private LocalDate dataEntrega;
	@Column(nullable = true)
	private int numeroQuestoes;

	public TipoProva getTipo() {
		return tipo;
	}

	public void setTipo(TipoProva tipo) {
		this.tipo = tipo;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

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




	public Cadastro getElaborador() {
		return elaborador;
	}




	public void setElaborador(Cadastro elaborador) {
		this.elaborador = elaborador;
	}


	public NivelProva getNivel() {
		return nivel;
	}

	public void setNivel(NivelProva nivel) {
		this.nivel = nivel;
	}

	public String getDescricao() {
		return descricao;
	}


	public Cadastro getRevisor1() {
		return revisor1;
	}

	public void setRevisor1(Cadastro revisor1) {
		this.revisor1 = revisor1;
	}

	public Cadastro getRevisor2() {
		return revisor2;
	}

	public void setRevisor2(Cadastro revisor2) {
		this.revisor2 = revisor2;
	}

	public Cadastro getRevisor3() {
		return revisor3;
	}

	public void setRevisor3(Cadastro revisor3) {
		this.revisor3 = revisor3;
	}

	public Cadastro getRevisorLinguagem() {
		return revisorLinguagem;
	}

	public void setRevisorLinguagem(Cadastro revisorLinguagem) {
		this.revisorLinguagem = revisorLinguagem;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	public Prova(){
		
	}
	

	
}
