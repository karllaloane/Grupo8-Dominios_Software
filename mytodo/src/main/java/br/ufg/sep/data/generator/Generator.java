package br.ufg.sep.data.generator;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import org.springframework.context.annotation.Bean;

import com.vaadin.flow.spring.annotation.SpringComponent;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.repositories.RoleUserRepository;
import br.ufg.sep.entity.Atendimento;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;

@SpringComponent
public class Generator {
	
	CadastroRepository cadastroRepository;
	RoleUserRepository roleUserRepository;

	QuestaoRepository questaoRepository;
	Generator(CadastroRepository cadastroRepository, RoleUserRepository roleUserRepository, QuestaoRepository questaoRepository){
		this.cadastroRepository = cadastroRepository;
		this.roleUserRepository = roleUserRepository;
		this.questaoRepository = questaoRepository;
	}
	@Bean
	public int testarState(){
		Questao q = new Questao();
if(true) return 0;

		//criando rev1
		Revisao revisao = new Revisao();
		revisao.setOrientacoes("TesteOrientacoes");
		HashMap<String,Atendimento> hashMap = new HashMap<>();
		hashMap.put("Paralelismo",Atendimento.NAO_ATENDIDA);
		hashMap.put("Contextualizacao",Atendimento.NAO_ATENDIDA);
		revisao.setItemAnalisado(hashMap);
		//criando corr1
		Correcao correcao = new Correcao();
		correcao.setJustificativaDoAtendimento("Teste");
		correcao.setAtendimentoSugestoes(Atendimento.PARCIAL);

		q.enviarParaRevisao(null); // r1
		questaoRepository.save(q);
		q.enviarParaBanca(revisao); // questao nao atendeu, e foi enviada para c1
		questaoRepository.save(q);
		q.enviarParaRevisao(correcao); // enviada para rev2, passando a corr1
		System.out.println("\n--------------\n"+q.getState().toString()+"\n--------------\n");
		questaoRepository.save(q);
		return 0;
//-> o que deve ter: 5 questao_state, o qual o ultimo é Revisao2
		//-> ter um HashMap mapeado

	}
	
	@Bean
	public void gerar(){
		if(cadastroRepository.count()<=0l) {
			Cadastro cadastro = new Cadastro();
			cadastro.setCpf("123");
			cadastro.setSenha("123");
			cadastro.setNome("Gabriel");
			
			RoleUser rU = new RoleUser(Role.ADMIN,cadastro.getCpf());
			
			
			roleUserRepository.save(rU);
			cadastroRepository.save(cadastro);
		}
		if(cadastroRepository.count()<=1l) {
			Cadastro c = new Cadastro();
			c.setCpf("456");
			c.setSenha("456");
			c.setNome("Guilherme");
			RoleUser roleUser1 = new RoleUser(Role.PED,c.getCpf());
			
			roleUserRepository.save(roleUser1);
			cadastroRepository.save(c);
			
			Cadastro c1 = new Cadastro();
			c1.setCpf("789");
			c1.setSenha("789");
			c1.setNome("Karlla");
			RoleUser roleUser2 = new RoleUser(Role.PROF,c1.getCpf());
			
			roleUserRepository.save(roleUser2);
			cadastroRepository.save(c1);
			
			Cadastro c2 = new Cadastro();
			c2.setCpf("111");
			c2.setSenha("111");
			RoleUser roleUser3 = new RoleUser(Role.USER,c2.getCpf());
			
				
			roleUserRepository.save(roleUser3);
			cadastroRepository.save(c2);
		}
		
		if(cadastroRepository.count()<=4l) {
			
			Cadastro master = new Cadastro();
			master.setCpf("999");
			master.setSenha("ooo");
			master.setEmail("master@gmail.com");
			master.setGrauInstrução("Graduação");
			master.setNome("João Gabriel");
			ArrayList<RoleUser> roles = new ArrayList<>();
			roles.add(new RoleUser(Role.ADMIN,master.getCpf()));
			roles.add(new RoleUser(Role.PED,master.getCpf()));
			roles.add(new RoleUser(Role.PROF,master.getCpf()));
			roles.add(new RoleUser(Role.USER,master.getCpf()));
		
			roles.forEach(e->{
				roleUserRepository.save(e);
			});
			
			cadastroRepository.save(master);
		}
		
	}

}
