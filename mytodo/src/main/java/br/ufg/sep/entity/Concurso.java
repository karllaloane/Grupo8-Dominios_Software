package br.ufg.sep.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Concurso extends AbstractEntity{
		
		@Column
		private String nome;
		
		@Column
		private String cidade;
		
		@Column
		private LocalDate dataInicio;
		
		@Column
		private LocalDate dataFim;
		
		@OneToMany(mappedBy="concurso", cascade = CascadeType.ALL)
		List<Prova> provas = new ArrayList<>();
		
//		@OneToMany(mappedBy="cadastro", cascade = CascadeType.ALL)
//		List<Cadastro> colaboradores;	
	
	
		public List<Prova> getProvas() {
			return provas;
		}

		public void setProvas(List<Prova> provas) {
			this.provas = provas;
		}


	public void addProvas(List<Prova> ps){
		ps.forEach(p->{
			this.provas.add(p);
		});
	}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getCidade() {
			return cidade;
		}

		public void setCidade(String cidade) {
			this.cidade = cidade;
		}

		public LocalDate getDataInicio() {
			return dataInicio;
		}

		public void setDataInicio(LocalDate dataInicio) {
			this.dataInicio = dataInicio;
		}

		public LocalDate getDataFim() {
			return dataFim;
		}

		public void setDataFim(LocalDate dataFim) {
			this.dataFim = dataFim;
		}

		/*
		 * public List<Cadastro> getColaboradores() { return colaboradores; }
		 * 
		 * public void setColaboradores(List<Cadastro> colaboradores) {
		 * this.colaboradores = colaboradores; }
		 */	
}
