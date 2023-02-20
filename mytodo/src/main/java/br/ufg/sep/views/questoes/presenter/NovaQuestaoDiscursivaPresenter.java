package br.ufg.sep.views.questoes.presenter;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.questoes.NovaQuestaoDiscursivaView;
import br.ufg.sep.views.questoes.NovaQuestaoObjetivaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class NovaQuestaoDiscursivaPresenter {
	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private NovaQuestaoDiscursivaView view;
	private Questao questao;
	private Prova prova;
	
	public NovaQuestaoDiscursivaPresenter(ProvaService provaService,
			QuestaoService questaoService, NovaQuestaoDiscursivaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		//chama o metodo salvar, que salva mas nao envia a questao para relaboracao
		view.getSalvarButton().addClickListener( e->salvarQuestao(e));
		
		//chama o metodo enviar, que salva e envia a questao para relaboracao
		view.getEnviarButton().addClickListener( e->enviarQuestao(e));	
	}
	
	private void enviarQuestao(ClickEvent<Button> event) {
		Notification notification;
		questao = new QuestaoDiscursiva();
		prova = view.getProva();
		
		//chama o método para coletar os dados da view
		//verificando se ele retornou ture, ou seja
		//todos os dados foram coletados com sucesso
		if(coletarDados()) {

			//envia para a correcao
			questao.enviarParaRevisao(null);
			
			//salva a questao
			questaoService.getRepository().save(questao);;
			
			/*Notifica ação bem sucedida*/
			notification = Notification
			        .show("Questão enviada para revisao 1 com sucesso!");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			
			event.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
		}
	}

	@SuppressWarnings("unchecked")
	private void salvarQuestao(ClickEvent<Button> event) {
		
		Notification notification;
		questao = new QuestaoDiscursiva();
		prova = view.getProva();
		
		//chama o método para coletar os dados da view
		//verificando se ele retornou ture, ou seja
		//todos os dados foram coletados com sucesso
		if(coletarDados()) {
			prova.getQuestoes().add(questao);
			
			provaService.save(prova);
			
			/*Notifica ação bem sucedida*/
			notification = Notification
			        .show("Questão salva com sucesso!");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			
			event.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
		}
		
	}
	
	private boolean coletarDados() {
		Notification notification;
		
		/* Criando e armazenando os valores do Input*/
		NivelDificuldade nivelSelecionado = view.getMetadados().getNivelDificuldadeCombo().getValue();
		String enunciado = view.getEnunciado().getValue();
		String respostaEsperada = view.getRespostaEsperada().getValue();
		List<String> subarea = view.getMetadados().getSubAreas();
							
		//pra nao deixar salvar sem selecionar o nivel de dificuldade
		if(nivelSelecionado == null) {
			notification = Notification
			        .show("Selecione o nível de dificuladade." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}

		//verificando demais campos em branco
		if(enunciado.isEmpty() || respostaEsperada.isEmpty() || subarea.size() == 0) {
			notification = Notification
			        .show("Campos em branco." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
		
		//setando os dados do objeto
		questao.setSubAreas(subarea);
		questao.setEnunciado(enunciado);
		((QuestaoDiscursiva) questao).setRespostaEsperada(respostaEsperada);
		questao.setNivelDificuldade(nivelSelecionado);
		questao.setProva(view.getProva());
		
		return true;
	}

}
