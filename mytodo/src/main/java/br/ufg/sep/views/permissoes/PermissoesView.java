package br.ufg.sep.views.permissoes;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.views.MainLayout;

@Route(value="permissoes", layout= MainLayout.class)
@RolesAllowed({"ADMIN"})
@PageTitle("Permissoes")
public class PermissoesView extends HorizontalLayout{
	
	CadastroRepository cadastroRepository;
	public PermissoesView(CadastroRepository cadastroRepository) {
		this.cadastroRepository = cadastroRepository;
		
		
		
	}

}
