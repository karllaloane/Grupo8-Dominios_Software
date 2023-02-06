package br.ufg.sep.views.concurso.presenter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.concurso.ConcursosView;
import br.ufg.sep.views.concurso.EditarConcursoView;
import br.ufg.sep.views.concurso.FormularioConcursoView;
import br.ufg.sep.views.concurso.VisualizarConcursoView;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;

public class ConcursoPresenter {
	
	ConcursoService concursoService;
	List<Concurso> c;
	LocalDateRenderer<Concurso> renderizadorDatasConcurso;
			
	public ConcursoPresenter(ConcursosView view, ConcursoService service) 
	{
		this.concursoService = service;
		renderizadorDatasConcurso = new LocalDateRenderer<>(Concurso::getDataFim,"dd/MM/yyyy");
		/***********************GRID************************/
		Grid<Concurso> concursos = new Grid<>(Concurso.class,false);
		concursos.setItems(query->
		service.getRepository().findAll(PageRequest.of(query.getOffset(), 
				query.getLimit())).stream());
		view.setGrid(concursos);
		
		
		view.getGrid().addSelectionListener(selection -> {
			Optional<Concurso> optionalConcurso = selection.getFirstSelectedItem();
            if (optionalConcurso.isPresent()) {
            	String teste = optionalConcurso.get().getNome();
            	c = service.getRepository().findByNome(teste);
            	view.habilitarButtons();
            }
		});
		/***********************GRID************************/

		
		/*** + Novo ****/
		view.getNovoButton().addClickListener(e->{
			view.getNovoButton().getUI().ifPresent(ui->{
			 ui.navigate(FormularioConcursoView.class); });
		});
	

		/*Visualizar*/
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(VisualizarConcursoView.class, c.get(0).getId());});
		});
		
		/*Editar*/
		view.getEditarButton().addClickListener(e->{
			
			view.getEditarButton().getUI().ifPresent(ui->{
				 ui.navigate(EditarConcursoView.class, c.get(0).getId());});
		});
		
		/*Acessar provas*/
		view.getAcessarProvasButton().addClickListener(e->{


			view.getAcessarProvasButton().getUI().ifPresent(ui->{
				 ui.navigate(GerenciarProvasView.class, c.get(0).getId());});
		});
		
		
		
	}
	public LocalDateRenderer<Concurso> getRenderizadorDatasConcurso() {
		return renderizadorDatasConcurso;
	}
	public void setRenderizadorDatasConcurso(LocalDateRenderer<Concurso> renderizadorDatasConcurso) {
		this.renderizadorDatasConcurso = renderizadorDatasConcurso;
	}
	


}
