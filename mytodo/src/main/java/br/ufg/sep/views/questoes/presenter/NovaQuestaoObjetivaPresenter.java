package br.ufg.sep.views.questoes.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufg.sep.entity.*;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.views.concurso.ConcursosView;
import br.ufg.sep.views.concurso.FormularioConcursoView;
import br.ufg.sep.views.questoes.NovaQuestaoObjetivaView;
import br.ufg.sep.views.questoes.VisualizarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class NovaQuestaoObjetivaPresenter {

	private ProvaService provaService;
	private QuestaoService questaoService;
	private NovaQuestaoObjetivaView view;
	private QuestaoObjetiva questao;
	private Prova prova;
	
	private int correta;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NovaQuestaoObjetivaPresenter(ProvaService provaService,
			QuestaoService questaoService, NovaQuestaoObjetivaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		correta = -1;
		
		//para cara checkbox da lista, ele vai adicionar um listener pra
		//verificar se algum foi marcado
		//caso algum tenha sido marcado, ele desmaca aqueles que são diferentes
		for(Checkbox cB : view.getCheckboxList()){
			cB.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChanged(ValueChangeEvent event) {
					Boolean value = (Boolean)(event.getValue());
					if(value.booleanValue()) {
						for(Checkbox c : view.getCheckboxList()) {
							if(c != cB) {
								c.setValue(false);
							}
						}
					} else {
						//aqui ele vai resetar a opcao correta
						//para que nao pege o valor errado,
						//caso ele marque e desmarque a mesma checkbox
						//sem marcar outra
						correta = -1;
					}
				}
				
			});
		}
		
		//chama o metodo salvar, que salva mas nao envia a questao para relaboracao
		view.getSalvarButton().addClickListener( e->salvarQuestao(e));
		
		//chama o metodo enviar, que salva e envia a questao para relaboracao
		view.getEnviarButton().addClickListener( e->enviarQuestao(e));	
	
	}

	private void enviarQuestao(ClickEvent<Button> event) {
		Notification notification;
		questao = new QuestaoObjetiva();
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
		questao = new QuestaoObjetiva();
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
		String justificativa = view.getJustificativaTA().getValue();
		List<String> subarea = view.getMetadados().getSubAreas();
		List<String> alternativasList = new ArrayList<String>();
		
		//verifica qual checkbox esta com valor verdadeiro
		//e marca a questao como correta
		for(int i = 0; i < view.getCheckboxList().size(); i++){
			if(view.getCheckboxList().get(i).getValue())
				correta  = i;
		}
		
		//pra nao deixar salvar sem marcar a questao correta
		if(correta == -1) {
			notification = Notification
			        .show("Selecione a alternativa correta.");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
		
		//pegando a lista de alternativas
		for(int i = 0; i < view.getAlternativasList().size(); i++) {
			alternativasList.add(view.getAlternativasList().get(i).getValue());
		}
		
		//verificando se tem alguma alternativa em branco
		for(String s : alternativasList) {
			if(s.isEmpty()) {
				notification = Notification
				        .show("Campos em branco." );
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
				return false;
			}
		}
				
		//pra nao deixar salvar sem selecionar o nivel de dificuldade
		if(nivelSelecionado == null) {
			notification = Notification
			        .show("Selecione o nível de dificuladade." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}

		//verificando demais campos em branco
		if(enunciado.isEmpty() || justificativa.isEmpty() || subarea.size() == 0) {
			notification = Notification
			        .show("Campos em branco." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
		
		//setando os dados do objeto
		questao.setAlternativaCorreta(correta);
		questao.setAlternativas(alternativasList);
		questao.setSubAreas(subarea);
		questao.setEnunciado(enunciado);
		questao.setJustificativa(justificativa);
		questao.setNivelDificuldade(nivelSelecionado);
		questao.setQuantAlternativas(view.getQuantAlternativas());
		questao.setProva(view.getProva());
		
		return true;
	}
}