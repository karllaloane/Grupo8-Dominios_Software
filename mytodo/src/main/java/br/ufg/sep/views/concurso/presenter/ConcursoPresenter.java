package br.ufg.sep.views.concurso.presenter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.concurso.ConcursosView;
import br.ufg.sep.views.concurso.EditarConcursoView;
import br.ufg.sep.views.concurso.FormularioConcursoView;
import br.ufg.sep.views.concurso.VisualizarConcursoView;

public class ConcursoPresenter {
	
	ConcursoService concursoService;
	List<Concurso> c;
	
	public ConcursoPresenter(ConcursosView view, ConcursoService service) 
	{
		this.concursoService = service;
		
		view.getNovoButton().addClickListener(e->{
			view.getNovoButton().getUI().ifPresent(ui->{
			 ui.navigate(FormularioConcursoView.class); });
		});
		
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
		
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(VisualizarConcursoView.class, c.get(0).getId());});
		});
		
		view.getEditarButton().addClickListener(e->{
			
			view.getEditarButton().getUI().ifPresent(ui->{
				 ui.navigate(EditarConcursoView.class, c.get(0).getId());});
		});
	}

}
