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
			RoleUser rU = new RoleUser(Role.ADMIN,cadastro);
			
			cadastro.setRoles(new ArrayList<RoleUser>(
					List.of(new RoleUser(
							Role.ADMIN
							,cadastro)
							)
						)
					);
			cadastroRepository.save(cadastro);
		}
	}

}
