package br.ufg.sep.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver{

	LoginForm login = new LoginForm();
	
	public LoginView(CadastroRepository cadastroRepository) {
		
		
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);
		login.setAction("login");
		
		add(
				new H1("SEP LOGIN"),
				login
				);
		
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {

		if(event.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			login.setError(true);
		}
				
		
	}
	
	
	
	
	
}
