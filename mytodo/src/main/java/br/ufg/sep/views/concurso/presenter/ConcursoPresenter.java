package br.ufg.sep.views.concurso.presenter;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.concurso.ConcursosView;

public class ConcursoPresenter {
	
	ConcursoService concursoService;
	public ConcursoPresenter(ConcursosView view, ConcursoService service) 
	{
		this.concursoService = service;
		
		Grid<Concurso> concursos = new Grid<>(Concurso.class,false);
		concursos.addColumn("nome").setAutoWidth(true);
		concursos.addColumn("cidade").setAutoWidth(true);
		concursos.addColumn("dataFim").setAutoWidth(true);
		concursos.setItems(query->
		service.getRepository().findAll(PageRequest.of(query.getOffset(), query.getLimit())).stream()
				);
		view.setGrid(concursos);
	}

}
