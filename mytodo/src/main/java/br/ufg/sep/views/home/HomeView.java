package br.ufg.sep.views.home;

import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.test.TestView;

@Route("")
@PageTitle("Home")
@PermitAll
public class HomeView extends HorizontalLayout{
	
	public HomeView(SecurityService secutiryService, CadastroRepository cr){
		
		
	
		
		
		Button arrumar = new Button("Navegar para teste");
		
		arrumar.addClickListener(e->{
			arrumar.getUI().ifPresent(ui->{
				ui.navigate(TestView.class);
			});
		});
		
		
		Button logout = new Button("log-out",e->{
			secutiryService.logout();		
		});
		
		
		
		add(new TextField(),logout,arrumar);
	}
	
}