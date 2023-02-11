package br.ufg.sep.views.questoes.presenter;

import java.util.Optional;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.test.TestView;
import br.ufg.sep.views.questoes.CadastrarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.VisualizarQuestoesProvaView;

public class VisualizarQuestoesProvaPresenter {

	private QuestaoService questaoService;
	private VisualizarQuestoesProvaView view;
	private Prova prova;
	
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
	}
}
