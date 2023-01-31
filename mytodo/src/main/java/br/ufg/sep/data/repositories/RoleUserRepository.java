package br.ufg.sep.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufg.sep.entity.role.RoleUser;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
	
	 List<RoleUser> findByUserCpf(String userCpf);

}
