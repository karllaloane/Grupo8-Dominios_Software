package br.ufg.sep.views.questoes.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.state.stateImpl.Concluida;
import br.ufg.sep.state.stateImpl.Correcao1;
import br.ufg.sep.state.stateImpl.Correcao2;
import br.ufg.sep.state.stateImpl.Elaboracao;
import br.ufg.sep.state.stateImpl.Revisao1;
import br.ufg.sep.state.stateImpl.Revisao2;
import br.ufg.sep.state.stateImpl.Revisao3;
import br.ufg.sep.state.stateImpl.RevisaoBanca;
import br.ufg.sep.state.stateImpl.RevisaoLinguagem;
import br.ufg.sep.views.correcao.CorrecaoDiscursivaBancaView;
import br.ufg.sep.views.correcao.CorrecaoObjetivaBancaView;
import br.ufg.sep.views.questoes.EditarQuestaoDiscursivaView;
import br.ufg.sep.views.questoes.EditarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.NovaQuestaoDiscursivaView;
import br.ufg.sep.views.questoes.NovaQuestaoObjetivaView;
import br.ufg.sep.views.questoes.VisualizarQuestaoObjetivaView;
import br.ufg.sep.views.revisao.RevisaoBancaView;
import br.ufg.sep.views.questoes.VisualizarQuestaoDiscursivaView;
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
		List<Questao> lista = questaoService.getRepository().findByProva(prova);
		List<Questao> listaRefinada = new ArrayList<>();
		lista.forEach(item->{
			if(item.getState()!=null)
				listaRefinada.add(item);
		});
		view.getQuestoesGrid().setItems(
				listaRefinada
                );
		
		//Evento do botao criar questao
		view.getNovaQuestaoButton().addClickListener(e->{
			
			if(prova.getTipo() == TipoProva.OBJETIVA_5 || prova.getTipo() == TipoProva.OBJETIVA_4)
				view.getNovaQuestaoButton().getUI().ifPresent(ui->{
				 ui.navigate(NovaQuestaoObjetivaView.class, prova.getId());});
			
			if(prova.getTipo() == TipoProva.DISCUSSIVA)
				view.getNovaQuestaoButton().getUI().ifPresent(ui->{
					 ui.navigate(NovaQuestaoDiscursivaView.class, prova.getId());});
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
            		
            		view.habilitarBotoesQuestao();
            		
            		//a ideia aqui é habilitar para os outros estados do elaborador
            		if(questao.getState() instanceof Elaboracao) {
            			view.getAcessarButton().setText("Visualizar");
            		}else if(questao.getState() instanceof Correcao1 
            				|| questao.getState() instanceof Correcao2 
            				|| questao.getState() instanceof RevisaoBanca) {
            			view.getAcessarButton().setText("Realizar correção");
            		} else {
            			view.getAcessarButton().setText("Acessar");
            		}
            	}
            }
		});
		
		/*Acessar a prova*/
		view.getAcessarButton().addClickListener(e->{
			
			//se a prova tiver com revisor, acessa mas não envia
			if((prova.getTipo() == TipoProva.OBJETIVA_4 || prova.getTipo() == TipoProva.OBJETIVA_5)
					&& (questao.getState() instanceof Elaboracao || questao.getState() instanceof Revisao1
							|| questao.getState() instanceof Revisao2
							|| questao.getState() instanceof Revisao3
							|| questao.getState() instanceof RevisaoLinguagem)) {
				
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(VisualizarQuestaoObjetivaView.class, questao.getId());});
			}
			
			//se a prova tiver com revisor, acessa mas não envia
			if((prova.getTipo() == TipoProva.DISCUSSIVA)
					&& (questao.getState() instanceof Elaboracao || questao.getState() instanceof Revisao1
					|| questao.getState() instanceof Revisao2
					|| questao.getState() instanceof Revisao3
					|| questao.getState() instanceof RevisaoLinguagem)) {
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(VisualizarQuestaoDiscursivaView.class, questao.getId());});
			}
			
			if((prova.getTipo() == TipoProva.OBJETIVA_4 || prova.getTipo() == TipoProva.OBJETIVA_5)
					&& (questao.getState() instanceof Correcao1 || questao.getState() instanceof Correcao2)) {
				
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(CorrecaoObjetivaBancaView.class, questao.getId());});
			}
			
			if((prova.getTipo() == TipoProva.DISCUSSIVA)
					&& (questao.getState() instanceof Correcao1 || questao.getState() instanceof Correcao2)) {
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(CorrecaoDiscursivaBancaView.class, questao.getId());});
			}
			
			if(questao.getState() instanceof RevisaoBanca) {
				view.getAcessarButton().getUI().ifPresent(ui->{
					ui.navigate(RevisaoBancaView.class, questao.getId());});
			}
			
			if((prova.getTipo() == TipoProva.OBJETIVA_4 || prova.getTipo() == TipoProva.OBJETIVA_5)
					&& questao.getState() instanceof Concluida ) {
				view.getAcessarButton().getUI().ifPresent(ui->{
					ui.navigate(VisualizarQuestaoObjetivaView.class, questao.getId());});
			}
			
			if((prova.getTipo() == TipoProva.DISCUSSIVA)
					&& questao.getState() instanceof Concluida ) {
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(VisualizarQuestaoDiscursivaView.class, questao.getId());});
			}
				
		});
		
		/*Excluir*/
		view.getExcluirButton().addClickListener(e->{
			
			view.getDialog().open();		
			
		});
		
		
		view.getEditarButton().addClickListener(e->{
			
			if((prova.getTipo() == TipoProva.OBJETIVA_4 || prova.getTipo() == TipoProva.OBJETIVA_5)) {
				
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(EditarQuestaoObjetivaView.class, questao.getId());});
			}
			
			if((prova.getTipo() == TipoProva.DISCUSSIVA)
					&& questao.getState() instanceof Elaboracao) {
				view.getAcessarButton().getUI().ifPresent(ui->{
					 ui.navigate(EditarQuestaoDiscursivaView.class, questao.getId());});
			}	
			
		});

		//evento do botao cancelar do dialogo
		view.getDialogCancelaButton().addClickListener(e->{
			view.getDialog().close();
		});
		
		//evento do botao deletar do diálogo
		view.getDialogDeletaButton().addClickListener(e->{
			questaoService.questaoRepository.delete(questao);
			view.getDialog().close();
			
			//para atualizar a página
			e.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
		});
		
	}
}
