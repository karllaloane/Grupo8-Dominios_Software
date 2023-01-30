package br.ufg.sep.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;

@Entity
public class Cadastro extends AbstractEntity{
		
		@Column
		private String nome;
		
		@Column
		private String cpf;
		
		@Column
		private String email;
		
		@Column
		private String senha;
		
		@Column
		private String grauInstrução;
		
		@ElementCollection(fetch = FetchType.LAZY, targetClass = Role.class)
		@Enumerated(EnumType.STRING) 
		@OneToMany(mappedBy="cadastro", cascade = CascadeType.ALL)
		List<RoleUser> roles;
		
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public List<RoleUser> getRoles() {
			return roles;
		}

		public void setRoles(List<RoleUser> roles) {
			this.roles = roles;
		}

		@OneToMany(mappedBy="responsavel", fetch = FetchType.LAZY,cascade= CascadeType.ALL)
		List<Prova> provas;
		

		

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
		//adicionar algo do postgre?
		@Column
		List<Map<Concurso,RoleUser>> papeis;
	
	
}
