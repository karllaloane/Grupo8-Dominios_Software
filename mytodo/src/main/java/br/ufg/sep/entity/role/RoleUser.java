package br.ufg.sep.entity.role;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.Table;

import br.ufg.sep.entity.AbstractEntity;


@Entity
@Table(name = "roles_user")
public class RoleUser extends AbstractEntity {
	

	private String userCpf;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	public RoleUser(Role r, String cpf) {
		this.role = r;
		this.userCpf = cpf;
	}
public RoleUser() {
	
}


	public String getUserCpf() {
		return userCpf;
	}

	public void setUserCpf(String userCpf) {
		this.userCpf = userCpf;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	

}
