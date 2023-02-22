package br.ufg.sep.views.revisao.presenter;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextArea;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.views.questoes.QuestoesProvaView;
import br.ufg.sep.views.revisao.RevisaoBancaView;

public class RevisaoBancaPresenter {

	private ProvaService provaService;
	private QuestaoService questaoService;
	private RevisaoBancaView view;
	private Prova prova;
	
	
	public RevisaoBancaPresenter(ProvaService provaService, QuestaoService questaoService,
			RevisaoBancaView revisaoBancaView) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		prova = view.getQuestao().getProva();
		
		
		
		//chama o diálogo para confirmar o envio da questao
		view.getEnviarButton().addClickListener( e->{
			Notification notification;
			
			if(!view.getCheckbox().getValue()) {
				/*Notifica ação bem sucedida*/
				notification = Notification
				        .show("Necessário confirmar a aprovação da questão.");
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				
				return;
			}
			
			//implementar posteriormente
			//if(verificaDadosPreenchidos()) {
				//view.getEnvioDialogo().getDialog().open();
			//}
				
		});	
		
		//botao descartar, retorna para página anterior
		view.getVoltarButton().addClickListener(e -> {
			view.getCancelarDialogo().getDialog().open();
		});
		
		//se confirmar o descarte, chama o método que irá salvar e enviar a questão
		view.getCancelarDialogo().getDescartarDialogButton().addClickListener(e->{
			view.getCancelarDialogo().getDialog().close();
			e.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
		});
		
		//se cancelar o descarte, simplesmente fecha o diálogo
		view.getCancelarDialogo().getcancelarDialogButton().addClickListener(e->{
			view.getCancelarDialogo().getDialog().close();
		});
	}

	private void enviarQuestao(ClickEvent<Button> e) {
		// TODO Auto-generated method stub
		
	}

	private boolean verificaDadosPreenchidos() {
		
		if(view.getQuestao() instanceof QuestaoDiscursiva) {
			
		}
		
		return true;
	}
	
	

}
