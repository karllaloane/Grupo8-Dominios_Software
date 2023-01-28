package br.ufg.sep.views;

import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.data.ProvaRepository;
import br.ufg.sep.data.QuestaoRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.role.Role;

@PageTitle("Main")
@Route(value = "")
public class MainView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public MainView(ProvaRepository provaRepo, QuestaoRepository questaoRepo, CadastroRepository cr) {
    	Cadastro c = new Cadastro();
    	
    	c.setCpf("dskljdds");
    	ArrayList<Prova> provas = new ArrayList<Prova>();
    	Prova p = new Prova();
    	p.setResponsavel(c);
    	provas.add(p);
    	c.setProvas(provas);
    	
    	ArrayList<Role> roles= new ArrayList<>();
    	
    	roles.add(Role.PED);
    	
    	c.setRole(roles);
    
    	
    	cr.save(c);

    	
    }

}
