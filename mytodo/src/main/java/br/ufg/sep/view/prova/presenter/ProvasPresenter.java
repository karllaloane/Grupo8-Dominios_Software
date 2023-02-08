package br.ufg.sep.view.prova.presenter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.view.prova.ProvasView;

public class ProvasPresenter {

	ProvaService provaService;
	private Prova provaSelecionada;
	
	public ProvasPresenter(ProvasView view, ProvaService service) {
		this.provaService = service;
		
		view.getProvas().setItems(query-> provaService.getRepository()
                .findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream());
		
		//adicionando listener para os eventos da view
		//grid
		view.getProvas().addSelectionListener(selection -> {
			Optional<Prova> optionalProva = selection.getFirstSelectedItem();
            if (optionalProva.isPresent()) {
                Long testeId = optionalProva.get().getId();
                Optional<Prova> talvezProva = provaService.getRepository().findById(testeId);
                if(talvezProva.isPresent()) {
                    provaSelecionada = talvezProva.get();
                    view.habilitarButtons();
                }
            }
        });
	}
	
}
