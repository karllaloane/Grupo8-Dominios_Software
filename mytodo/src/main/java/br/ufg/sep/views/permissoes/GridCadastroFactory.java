package br.ufg.sep.views.permissoes;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.entity.Cadastro;


public class GridCadastroFactory{
	
	public Grid<Cadastro> grid;
	public GridCadastroFactory(CadastroRepository cadastroRepository) {
		grid = new Grid<>(Cadastro.class,false);
		
		grid.addColumn("cpf").setAutoWidth(true);
		grid.addColumn("nome").setAutoWidth(true);
		grid.addColumn("email").setAutoWidth(true);
		grid.setItems(query-> cadastroRepository.findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream());
	}
	
	public Grid<Cadastro> getGrid(){
		return this.grid;
	}
	

}
