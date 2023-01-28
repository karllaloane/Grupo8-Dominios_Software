package br.ufg.sep.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import br.ufg.sep.entity.role.Role;

@Entity
public class Cadastro extends AbstractEntity{
		
		@Column
		private String nome;
		
		@Column
		private String cpf;
		
		@Column
		private String senha;
		
		@Column
		private String grauInstrução;
		
		@ElementCollection(fetch = FetchType.LAZY, targetClass = Role.class)
		@Enumerated(EnumType.STRING) 
		@Column(name = "role")
		@JoinTable(name="Roles")
		List<Role> role;
		
		@OneToMany(mappedBy="responsavel", fetch = FetchType.LAZY,cascade= CascadeType.ALL)
		List<Prova> provas;
		

		public List<Role> getRole() {
			return role;
		}

		public void setRole(List<Role> role) {
			this.role = role;
		}

		public List<Prova> getProvas() {
			return provas;
		}

		public void setProvas(List<Prova> provas) {
			this.provas = provas;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getCpf() {
			return cpf;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

		public String getGrauInstrução() {
			return grauInstrução;
		}

		public void setGrauInstrução(String grauInstrução) {
			this.grauInstrução = grauInstrução;
		}
		
		
		
		// map complicado de papeis
	
	
}
