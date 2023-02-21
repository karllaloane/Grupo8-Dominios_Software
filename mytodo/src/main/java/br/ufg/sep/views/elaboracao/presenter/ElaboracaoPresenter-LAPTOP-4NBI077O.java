package br.ufg.sep.views.elaboracao.presenter;

import java.util.Optional;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.security.AuthenticatedUser;
import br.ufg.sep.views.elaboracao.ElaboracaoView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class ElaboracaoPresenter {

	ProvaService provaService;
	private Prova provaSelecionada;
	Prova prova;
	
	public ElaboracaoPresenter(AuthenticatedUser authenticatedUser, ElaboracaoView view, ProvaService service) {
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
		
		view.getGridProvas().addItemDoubleClickListener(event -> {
			prova = event.getItem();
			
			if(prova != null) {
				view.getVisualizarButton().getUI().ifPresent(ui->{
					 ui.navigate(QuestoesProvaView.class, prova.getId());});
			}
			
			
		});
		
		/*Visualizar*/
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(QuestoesProvaView.class, prova.getId());});
		});
	}
	
	
}
