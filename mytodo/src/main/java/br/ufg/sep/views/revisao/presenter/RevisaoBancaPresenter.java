package br.ufg.sep.views.revisao.presenter;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextArea;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.questoes.QuestoesProvaView;
import br.ufg.sep.views.revisao.RevisaoBancaView;

public class RevisaoBancaPresenter {

	private ProvaService provaService;
	private QuestaoService questaoService;
	private RevisaoBancaView view;
	private Prova prova;
	private Questao questao;
	
	public RevisaoBancaPresenter(ProvaService provaService, QuestaoService questaoService,
			RevisaoBancaView view) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		this.questao = view.getQuestao();
		
		prova = view.getQuestao().getProva();
		
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
			
			if(questao instanceof QuestaoObjetiva) {
				getDadosObjetiva();
			} else {
				getDadosSubjetiva();
			}
			
			questao.concluir();
			
			questaoService.getRepository().save(questao);
			
			/*Notifica ação bem sucedida*/
			notification = Notification
			        .show("Questão concluída com sucesso!");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			
			e.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
			
			//implementar posteriormente
			//if(verificaDadosPreenchidos()) {
				
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

	private void getDadosSubjetiva() {
		
		questao.setEnunciado(view.getQuestaoNova().getEnunciado().getValue());
		((QuestaoDiscursiva) questao).setRespostaEsperada(view.getQuestaoNova().getRespostaEsperada().getValue());
		
	}

	private void getDadosObjetiva() {

		List<String> lista = new ArrayList<>();
		
		lista.add(view.getQuestaoNova().getAlternativaA().getValue());
		lista.add(view.getQuestaoNova().getAlternativaB().getValue());
		lista.add(view.getQuestaoNova().getAlternativaC().getValue());
		lista.add(view.getQuestaoNova().getAlternativaD().getValue());
		
		if(((QuestaoObjetiva) questao).getQuantAlternativas() == 5)
			lista.add(view.getQuestaoNova().getAlternativaE().getValue());
		
		questao.setEnunciado(view.getQuestaoNova().getEnunciado().getValue());

		((QuestaoObjetiva) questao).setAlternativas(lista);
		
	}

	private boolean verificaDadosPreenchidos() {
		
		if(view.getQuestao() instanceof QuestaoDiscursiva) {
			
		}
		
		return true;
	}
	
	

}
