package br.ufg.sep.data.services;

import org.springframework.stereotype.Service;

import br.ufg.sep.data.repositories.RoleUserRepository;
import br.ufg.sep.entity.role.RoleUser;

@Service
public class RoleUserService {
	
private final RoleUserRepository roleUserRepository;

public RoleUserService(RoleUserRepository rUr) {
	this.roleUserRepository = rUr;
	
}

public RoleUserRepository getRoleUserRepository() {
	return roleUserRepository;
}

public void save(RoleUser roleUser) throws java.lang.NullPointerException{
	
	if(roleUser.getUserCpf()==null) {
		throw new NullPointerException("CPF ESTÁ NULO!");
	}
	
	roleUserRepository.save(roleUser);
	
}

	
	
}
