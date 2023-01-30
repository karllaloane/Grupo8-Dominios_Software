package br.ufg.sep.data;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufg.sep.entity.role.RoleUser;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
	
	 RoleUser findByUserCpf(String userCpf);

}
