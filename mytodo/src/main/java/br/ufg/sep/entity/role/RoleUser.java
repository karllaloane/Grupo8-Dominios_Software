package br.ufg.sep.entity.role;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.ufg.sep.entity.AbstractEntity;
import br.ufg.sep.entity.Cadastro;

@Entity
@Table(name = "roles_user")
public class RoleUser extends AbstractEntity {
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="cadastro_id", nullable = false)
	private Cadastro cadastro;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	public Role getRole() {
		return role;
	}
	public Cadastro getCadastro() {
		return cadastro;
	}
	public void setCadastro(Cadastro cadastro) {
		this.cadastro = cadastro;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
