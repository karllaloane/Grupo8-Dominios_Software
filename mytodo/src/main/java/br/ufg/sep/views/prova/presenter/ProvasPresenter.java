package br.ufg.sep.view.prova.presenter;

import java.util.Optional;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.security.AuthenticatedUser;
import br.ufg.sep.view.prova.ProvasView;
import br.ufg.sep.views.questoes.VisualizarQuestoesProvaView;

public class ProvasPresenter {

	ProvaService provaService;
	private Prova provaSelecionada;
	Prova prova;
	
	public ProvasPresenter(AuthenticatedUser authenticatedUser, ProvasView view, ProvaService service) {
		this.provaService = service;
		
		Cadastro user;
		
		Optional<Cadastro> maybeUser = authenticatedUser.get();
		if(maybeUser.isPresent()) {
			user = maybeUser.get();
        
			//populando a view
			view.getGridProvas().setItems(provaService.getRepository()
	                .findByElaborador(user));
		}
		
		//adicionando listener para os eventos da view
		//grid
		view.getGridProvas().addSelectionListener(selection -> {
			Optional<Prova> optionalProva = selection.getFirstSelectedItem();
            if (optionalProva.isPresent()) {
                Long testeId = optionalProva.get().getId();
                Optional<Prova> talvezProva = provaService.getRepository().findById(testeId);
                if(!talvezProva.isEmpty())
                	prova = talvezProva.get();
                	view.habilitarButtons();
            }
        });
		
		/*Visualizar*/
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(VisualizarQuestoesProvaView.class, prova.getId());});
		});
	}
	
	
}
