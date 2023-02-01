package br.ufg.sep.views.permissoes;



import javax.swing.Popup;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;


public class GridCadastroFactory{
	
	Button novoButton = new Button();
	Button editarButton = new Button();
	Button visualizarButton = new Button();
	HorizontalLayout layout =  new HorizontalLayout();
	
	public Grid<Cadastro> grid;
	
	public void habilitarButtons() {
		editarButton.setEnabled(true);
		visualizarButton.setEnabled(true);
	}
	
	public GridCadastroFactory(CadastroRepository cadastroRepository) {
		grid = new Grid<>(Cadastro.class,false);
		
		grid.addColumn("cpf").setAutoWidth(true);
		grid.addColumn("nome").setAutoWidth(true);
		grid.addColumn("email").setAutoWidth(true);
		
		layout.add(novoButton, visualizarButton, editarButton);
		
		grid.setItems(query-> cadastroRepository.findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream());
	}
	
	public Grid<Cadastro> getGrid(){
		return this.grid;
	}
	
	public Button getNovoButton() {
		return novoButton;
	}
	
	public Button getEditarButton() {
		return editarButton;
	}
	
	public Button getVisualizarButton() {
		return visualizarButton;
	}
	
	public Button getAcessarProvasButton() {
		return visualizarButton;
	}
	
	
	
	
	
	
	
	

}


