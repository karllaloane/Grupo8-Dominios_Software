package br.ufg.sep.views.questoes.presenter;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import antlr.debug.Event;

import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.views.questoes.EditarQuestaoDiscursivaView;
import br.ufg.sep.views.questoes.NovaQuestaoDiscursivaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class EditarQuestaoDiscursivaPresenter {

	private ProvaService provaService;
	private QuestaoService questaoService;
	private EditarQuestaoDiscursivaView view;
	private Questao questao;
	private Prova prova;
	
	//variaveis para coletar campos da view
	private NivelDificuldade nivelSelecionado;
	private String enunciado;
	private String respostaEsperada;
	private List<String> subarea;
	
	public EditarQuestaoDiscursivaPresenter(ProvaService provaService, QuestaoService questaoService,
			EditarQuestaoDiscursivaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		//chama o metodo salvar, que salva mas nao envia a questao para relaboracao
		view.getSalvarButton().addClickListener( e->salvarQuestao(e));
		
		//chama o diálogo para confirmar o envio da questao
		view.getEnviarButton().addClickListener( e->{
			if(verificaDadosPreenchidos())
				view.getEnvioDialogo().getDialog().open();
		});	
		
		//botao cancelar, retorna para página anterior
		view.getCancelarButton().addClickListener(e -> {
			view.getCancelarDialogo().getDialog().open();
		});
		
		/**pegando as ações dos diálogos**/
		
		//se confirmar o envio, chama o método que irá salvar e enviar a questão
		view.getEnvioDialogo().getEnviarDialogButton().addClickListener(e->{
			view.getEnvioDialogo().getDialog().close();
			enviarQuestao(e);
		});
		
		//se cancelar o envio, simplesmente fecha o diálogo
		view.getEnvioDialogo().getcancelarDialogButton().addClickListener(e->{
			view.getEnvioDialogo().getDialog().close();
		});
		
		//se confirmar o descarte, chama o método que irá salvar e enviar a questão
		view.getCancelarDialogo().getDescartarDialogButton().addClickListener(e->{
			view.getCancelarDialogo().getDialog().close();
			e.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, view.getProva().getId()));
		});
		
		//se cancelar o descarte, simplesmente fecha o diálogo
		view.getCancelarDialogo().getcancelarDialogButton().addClickListener(e->{
			view.getCancelarDialogo().getDialog().close();
		});
	}
	
	private void enviarQuestao(ClickEvent<Button> event) {
		Notification notification;
		questao = new QuestaoDiscursiva();
		prova = view.getProva();
		
		//chama o método para coletar os dados da view
		//verificando se ele retornou ture, ou seja
		//todos os dados foram coletados com sucesso
		if(verificaDadosPreenchidos()) {
			criarQuestao();
			//setando o id para salvar a mesma questao
			questao.setId(view.getQuestaoDiscursiva().getId());
			
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
	
	public void salvarQuestao(ClickEvent<Button> event) {
		Notification notification;
		questao = new QuestaoDiscursiva();
		prova = view.getProva();
		
		//chama o método para coletar os dados da view
		//verificando se ele retornou ture, ou seja
		//todos os dados foram coletados com sucesso
		if(verificaDadosPreenchidos()) {
			criarQuestao();
			questao.setId(view.getQuestaoDiscursiva().getId());
			
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
	
	private void criarQuestao() {
		questao = new QuestaoDiscursiva();
		
		//setando os dados do objeto
		questao.setSubAreas(subarea);
		questao.setEnunciado(enunciado);
		((QuestaoDiscursiva) questao).setRespostaEsperada(respostaEsperada);
		questao.setNivelDificuldade(nivelSelecionado);
		questao.setProva(view.getProva());

	}
	
	public boolean verificaDadosPreenchidos() {
		
		Notification notification;
		
		/* armazenando os valores do Input*/
		this.nivelSelecionado = view.getMetadados().getNivelDificuldadeCombo().getValue();
		this.enunciado = view.getEnunciado().getValue();
		this.respostaEsperada = view.getRespostaEsperada().getValue();
		this.subarea = view.getMetadados().getSubAreas();
							
		//pra nao deixar salvar sem selecionar o nivel de dificuldade
		if(this.nivelSelecionado == null) {
			notification = Notification
			        .show("Selecione o nível de dificuladade." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
		
		//verificando demais campos em branco
		if(this.enunciado.isEmpty() || this.respostaEsperada.isEmpty() || this.subarea.size() == 0) {
			notification = Notification
			        .show("Campos em branco." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
		
		return true;		
	}

}
