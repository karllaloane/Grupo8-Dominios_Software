package br.ufg.sep.views.questoes.presenter;

import java.util.Optional;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.questoes.CadastrarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.VisualizarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.VisualizarQuestoesProvaView;

public class VisualizarQuestoesProvaPresenter {

	private QuestaoService questaoService;
	private VisualizarQuestoesProvaView view;
	private Prova prova;
	private Questao questao;
	
	public VisualizarQuestoesProvaPresenter(ProvaService provaService,
			QuestaoService questaoService, VisualizarQuestoesProvaView view) {
		this.questaoService = questaoService;
		this.view = view;
		
		//buscando a prova
		Optional<Prova> optionalProva = provaService.getRepository().findById(view.getProvaId());
		if(optionalProva.isPresent()) {
			prova = optionalProva.get();
		}
		
		//populando a view com as questoes daquela prova
		view.getQuestoesGrid().setItems(
                questaoService.getRepository().findByProva(prova));
		
		//Evento do botao criar questao
		view.getNovaQuestaoButton().addClickListener(e->{
			
			if(prova.getTipo() == TipoProva.OBJETIVA_5 || prova.getTipo() == TipoProva.OBJETIVA_4)
			view.getNovaQuestaoButton().getUI().ifPresent(ui->{
				 ui.navigate(CadastrarQuestaoObjetivaView.class, prova.getId());});
		});
		
		//listener da grid, busca a questao pelo id
		view.getQuestoesGrid().addSelectionListener(selection -> {
			Optional<Questao> optionalQuestao = selection.getFirstSelectedItem();
            if (optionalQuestao.isPresent()) {
            	Long teste = optionalQuestao.get().getId();
            	Optional<Questao> q = questaoService.getRepository().findById(teste);
            	if(q.isPresent()) {
            		questao = q.get();
            		view.habilitarButtons();
            	}
            }
		});
		
		/*Visualizar*/
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(VisualizarQuestaoObjetivaView.class, questao.getId());});
		});
	}
}
