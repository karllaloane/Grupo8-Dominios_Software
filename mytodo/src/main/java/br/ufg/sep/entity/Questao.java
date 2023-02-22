package br.ufg.sep.entity;

import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.stateImpl.Elaboracao;

import java.util.List;

import javax.persistence.*;

@Entity
public class Questao extends AbstractEntity {

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	QuestaoState state;
	@ManyToOne()
	@JoinColumn(name="prova_id", nullable = true)
	private Prova prova;
	
	private int idQuestao;
	@Column(length = 2054)
	private String enunciado;
	
	@ElementCollection
	private List<String> subAreas;
	
	@Enumerated(EnumType.STRING)
	private NivelDificuldade nivelDificuldade;
	
	//private String descricao;
	
	private String justificativa;
	
	@OneToOne(cascade = CascadeType.ALL)
	private AnexoQuestao anexo;
	
	
	public Questao(){
		this.state = new Elaboracao();
	}
	public Questao(int num) {
		this.idQuestao = num;
	}
	public Questao(String s){

	}


	/************ Métodos STATE********/
	public QuestaoState getState() {
		return state;
	}

	public void setState(QuestaoState state) {
		this.state = state;
	}
	public boolean enviarParaRevisao(Correcao correcao){
	return this.state.enviarParaRevisao(this,correcao);
	}

	public boolean enviarParaBanca(Revisao revisao){
		return this.state.enviarParaBanca(this,revisao);
	}

	public boolean enviarParaRevisaoLinguagem(Revisao revisao) {
	return this.state.enviarParaRevisaoLinguagem(this,revisao);
	}

	public boolean concluir(){
		return this.state.concluir(this);
	}

	public boolean descartar(Revisao revisao){
		return this.state.descartar(this,revisao);
	}

	public boolean guardarNoBanco() {
	return this.state.guardarNoBanco(this);
	}


	/************ Métodos STATE********/




	
	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public List<String> getSubAreas() {
		return subAreas;
	}

	public void setSubAreas(List<String> subareas) {
		this.subAreas = subareas;
	}

	public NivelDificuldade getNivelDificuldade() {
		return nivelDificuldade;
	}

	public void setNivelDificuldade(NivelDificuldade nivelDificuldade) {
		this.nivelDificuldade = nivelDificuldade;
	}

//	public String getDescricao() {
//		return descricao;
//	}
//
//	public void setDescricao(String descricao) {
//		this.descricao = descricao;
//	}

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

	public int getIdQuestao() {
		return idQuestao;
	}

	public void setIdQuestao(int idQuestao) {
		this.idQuestao = idQuestao;
	}

	
	

}
