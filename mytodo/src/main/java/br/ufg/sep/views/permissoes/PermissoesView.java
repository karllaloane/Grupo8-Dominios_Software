package br.ufg.sep.views.permissoes;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.views.MainLayout;

@Route(value="permissoes", layout= MainLayout.class)
@PermitAll
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
