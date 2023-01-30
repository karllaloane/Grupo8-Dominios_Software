package br.ufg.sep.test;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("test")
@PageTitle("Home")
@RolesAllowed({"ADMIN","PROF"})
public class TestView extends HorizontalLayout {
	
	public TestView(){
		
		add(new Button("TESTE"));
	}

}