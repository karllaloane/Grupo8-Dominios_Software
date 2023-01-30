package br.ufg.sep.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;

import com.vaadin.flow.spring.annotation.SpringComponent;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;

@SpringComponent
public class Generator {
	
	CadastroRepository cadastroRepository;
	Generator(CadastroRepository cadastroRepository){
		this.cadastroRepository = cadastroRepository;
	}
	
	@Bean
	public void gerar(){
		if(cadastroRepository.count()==0l) {
			Cadastro cadastro = new Cadastro();
			cadastro.setCpf("123");
			cadastro.setSenha("123");
			
<<<<<<< HEAD
			
			RoleUser rU = new RoleUser(Role.ADMIN,cadastro);
			ArrayList<RoleUser> rolesUsers = new ArrayList<>();
			rolesUsers.add(rU);
			
			cadastro.setRoles(rolesUsers);
			
			/*
			 * Cria o array list de RoleUser
			 * Define que esse arrayList é uma lista de: 
			 * 	novo usuario, o qual tem a role como Role.ADMIN e tem o pai 'cadastro' 
			 * 
			cadastro.setRoles(new ArrayList<RoleUser>(
					List.of(new RoleUser(
							Role.ADMIN
							,cadastro)
							)
						)
					);
					*/
=======
			cadastro.setRoles(new ArrayList<RoleUser>(List.of(rU)));
>>>>>>> 1ed80f393f241c2764f5d8c831d91a3a40c1613a
			cadastroRepository.save(cadastro);
		}
		if(cadastroRepository.count()==1l) {
			Cadastro c = new Cadastro();
			c.setCpf("456");
			c.setSenha("456");
			RoleUser roleUser1 = new RoleUser(Role.PED,c);
			
			c.setRoles(new ArrayList<RoleUser>(List.of(roleUser1)));
			cadastroRepository.save(c);
			
			Cadastro c1 = new Cadastro();
			c1.setCpf("789");
			c1.setSenha("789");
			RoleUser roleUser2 = new RoleUser(Role.PROF,c1);
			
			c1.setRoles(new ArrayList<RoleUser>(List.of(roleUser2)));
			cadastroRepository.save(c1);
			
			Cadastro c2 = new Cadastro();
			c2.setCpf("111");
			c2.setSenha("111");
			RoleUser roleUser3 = new RoleUser(Role.USER,c2);
			
			c2.setRoles(new ArrayList<RoleUser>(List.of(roleUser3)));
			cadastroRepository.save(c2);
		}
		
	}

}
