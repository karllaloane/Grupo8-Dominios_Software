package br.ufg.sep.views.permissoes;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.views.MainLayout;

@Route(value="permissoes", layout= MainLayout.class)
@RolesAllowed({"ADMIN"})
@PageTitle("Permissoes")
public class PermissoesView extends HorizontalLayout{
	
	CadastroRepository cadastroRepository;
	
	private Grid<Cadastro> cadastros;
	
	public PermissoesView(CadastroRepository cadastroRepository) {
		this.cadastroRepository = cadastroRepository;
		
		this.cadastros = new GridCadastroFactory(cadastroRepository).getGrid();
		
		add(cadastros);
		
	}

}