package br.ufg.sep.views.home;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.security.SecurityService;

@Route("")
@PageTitle("Home")
@PermitAll
public class HomeView extends HorizontalLayout{
	
	public HomeView(SecurityService secutiryService){
		
		Button logout = new Button("log-out",e->{
			secutiryService.logout();
			
		});
		
		add(new TextField("Ola"),logout);
	}
	
}
