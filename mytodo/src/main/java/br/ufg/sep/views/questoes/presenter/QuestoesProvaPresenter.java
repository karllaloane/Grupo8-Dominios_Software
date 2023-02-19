package br.ufg.sep.views.questoes.presenter;

import java.util.Optional;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.state.stateImpl.Elaboracao;
import br.ufg.sep.views.questoes.NovaQuestaoObjetivaView;
import br.ufg.sep.views.questoes.VisualizarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class QuestoesProvaPresenter {

	private QuestaoService questaoService;
	private QuestoesProvaView view;
	private Prova prova;
	private Questao questao;
	
	public QuestoesProvaPresenter(ProvaService provaService,
			QuestaoService questaoService, QuestoesProvaView view) {
		this.questaoService = questaoService;
		this.view = view;
				
		//buscando a prova
		Optional<Prova> optionalProva = provaService.getRepository().findById(view.getProvaId());
		if(optionalProva.isPresent()) {
			prova = optionalProva.get();
		}
		
		//impedindo que sejam criadas mais questões além do que foi solicitado
		//é regra de negócio, deveria verificar no service, mas....
		if(prova.getQuestoes().size() == prova.getNumeroQuestoes()) {
			view.getNovaQuestaoButton().setEnabled(false);
		}
		
		//populando a view com as questoes daquela prova
		view.getQuestoesGrid().setItems(
                questaoService.getRepository().findByProva(prova));
		
		//Evento do botao criar questao
		view.getNovaQuestaoButton().addClickListener(e->{
			
			if(prova.getTipo() == TipoProva.OBJETIVA_5 || prova.getTipo() == TipoProva.OBJETIVA_4)
			view.getNovaQuestaoButton().getUI().ifPresent(ui->{
				 ui.navigate(NovaQuestaoObjetivaView.class, prova.getId());});
		});
		
		//listener da grid, busca a questao pelo id
		view.getQuestoesGrid().addSelectionListener(selection -> {
			Optional<Questao> optionalQuestao = selection.getFirstSelectedItem();
            if (optionalQuestao.isPresent()) {
            	Long teste = optionalQuestao.get().getId();
            	Optional<Questao> q = questaoService.getRepository().findById(teste);
            	if(q.isPresent()) {
            		questao = q.get();
            		
            		//só permitir deletar e editar a questao se ela tiver em elaboracao
            		if(questao.getState() instanceof Elaboracao) {
            			view.getExcluirButton().setVisible(true);
            			view.getEditarButton().setVisible(true);
            		} else {
            			view.getExcluirButton().setVisible(false);
            			view.getEditarButton().setVisible(false);
            		}
            		
            		//a ideia aqui é habilitar para os outros estados do elaborador
            		if(questao.getState() instanceof Elaboracao) {
            			view.habilitarBotoesQuestao();
            		} else {
            			view.desabilitarBotoesQuestao();
            		}
            	}
            }
		});
		
		/*Visualizar*/
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(VisualizarQuestaoObjetivaView.class, questao.getId());});
		});
		
		/*Visualizar*/
		view.getExcluirButton().addClickListener(e->{
			
			view.getDialog().open();		
			
		});
		
		view.getDialogCancelaButton().addClickListener(e->{
			view.getDialog().close();
		});
		
		view.getDialogDeletaButton().addClickListener(e->{
			questaoService.questaoRepository.delete(questao);
			view.getDialog().close();
			
			//para atualizar a página
			e.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
		});
		
	}
}
