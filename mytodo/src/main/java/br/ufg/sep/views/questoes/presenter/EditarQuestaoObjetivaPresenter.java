package br.ufg.sep.views.questoes.presenter;

import java.util.ArrayList;
import java.util.List;

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
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.questoes.EditarQuestaoObjetivaView;
import br.ufg.sep.views.questoes.NovaQuestaoObjetivaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class EditarQuestaoObjetivaPresenter {

	private ProvaService provaService;
	private QuestaoService questaoService;
	private EditarQuestaoObjetivaView view;
	private QuestaoObjetiva questao;
	private Prova prova;
	
	private int correta;
	
	//variáveis para coletar os dados da view
	private NivelDificuldade nivelSelecionado;
	private String enunciado;
	private String justificativa;
	private List<String> subarea;
	private List<String> alternativasList;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EditarQuestaoObjetivaPresenter(ProvaService provaService, QuestaoService questaoService,
			EditarQuestaoObjetivaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		prova = view.getQuestaoObjetiva().getProva();
		
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
		
		//chama o diálogo para confirmar o envio da questao
		view.getEnviarButton().addClickListener( e->{
			if(verificaDadosPreenchidos())
				view.getEnvioDialogo().getDialog().open();
		});	
		
		//botao cancelar, retorna para página anterior
		view.getDescartarButton().addClickListener(e -> {
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
			e.getSource().getUI().ifPresent(ui -> ui.navigate(QuestoesProvaView.class, prova.getId()));
		});
		
		//se cancelar o descarte, simplesmente fecha o diálogo
		view.getCancelarDialogo().getcancelarDialogButton().addClickListener(e->{
			view.getCancelarDialogo().getDialog().close();
		});

	}

	private void enviarQuestao(ClickEvent<Button> event) {
		Notification notification;
		questao = new QuestaoObjetiva();
		
		//chama o método para verificar os dados da view
		//se ele retornou true todos os dados foram coletados com sucesso
		if(verificaDadosPreenchidos()) {
			//cria a questão com os dados coletados
			criarQuestao();

			//setando o id para update
			questao.setId(view.getQuestaoObjetiva().getId());
			
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

		//chama o método para verificar os dados da view
		//se ele retornou true todos os dados foram coletados com sucesso
		if(verificaDadosPreenchidos()) {
			//cria a questão com os dados coletados
			criarQuestao();
			
			//setando o id para update
			questao.setId(view.getQuestaoObjetiva().getId());
			
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
	
	private boolean verificaDadosPreenchidos() {
		Notification notification;
		
		/* Criando e armazenando os valores do Input*/
		nivelSelecionado = view.getMetadados().getNivelDificuldadeCombo().getValue();
		enunciado = view.getEnunciado().getValue();
		justificativa = view.getJustificativaTA().getValue();
		subarea = view.getMetadados().getSubAreas();
		alternativasList = new ArrayList<String>();
		
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
		
		return true;
	}
	
	private void criarQuestao() {
		questao = new QuestaoObjetiva();
		
		//setando os dados do objeto
		questao.setAlternativaCorreta(correta);
		questao.setAlternativas(alternativasList);
		questao.setSubAreas(subarea);
		questao.setEnunciado(enunciado);
		questao.setJustificativa(justificativa);
		questao.setNivelDificuldade(nivelSelecionado);
		questao.setQuantAlternativas(view.getQuantAlternativas());
		questao.setProva(prova);
	}
}

