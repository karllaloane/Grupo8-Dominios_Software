package br.ufg.sep.views.home;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.role.Role;
import br.ufg.sep.entity.role.RoleUser;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.test.TestView;
import br.ufg.sep.views.MainLayout;

@Route(value="", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll
public class HomeView extends HorizontalLayout{
	
	
	public HomeView(SecurityService secutiryService, CadastroRepository cr){
	
		
	
	}

	
}