package br.ufg.sep.view.prova;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.views.MainLayout;

@Route(value="provas", layout = MainLayout.class)
@PageTitle("Provas")
@PermitAll
public class ProvasView extends HorizontalLayout{
	
	public ProvasView(SecurityService secutiryService, CadastroRepository cr){
	
		Cadastro cadastro = new Cadastro();
		
		Button arrumar = new Button("PROVAAAAAS");
		
		
		add(new TextField(),arrumar);
	}

}
