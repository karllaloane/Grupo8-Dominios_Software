package br.ufg.sep.test;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.views.MainLayout;

@Route(value = "test", layout = MainLayout.class)
@PageTitle("Home")
@RolesAllowed({"ADMIN"})
public class TestView extends HorizontalLayout {
	
	public TestView(){
		
		add(new Button("TESTE"));
	}

}