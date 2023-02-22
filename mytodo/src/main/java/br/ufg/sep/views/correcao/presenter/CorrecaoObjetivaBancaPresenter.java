package br.ufg.sep.views.correcao.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributeIdentifierException;

import com.nimbusds.jose.JWEObject.State;
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
import br.ufg.sep.entity.Atendimento;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.stateImpl.Correcao1;
import br.ufg.sep.state.stateImpl.Correcao2;
import br.ufg.sep.views.correcao.CorrecaoObjetivaBancaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class CorrecaoObjetivaBancaPresenter {
	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private CorrecaoObjetivaBancaView view;
	
	private int correta;
	
	//variáveis para coletar os dados da view
	private String enunciado;
	private String justificativaCorreta;
	private List<String> alternativasList;
	private String justificativaAtendimento;
	private Atendimento atendimento;
	
	private Prova prova; 
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CorrecaoObjetivaBancaPresenter(ProvaService provaService, QuestaoService questaoService,
			CorrecaoObjetivaBancaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		prova = view.getProva();
		
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
			
		//chama o diálogo para confirmar o envio da questao
		view.getEnviarButton().addClickListener( e->{
			if(verificaDadosPreenchidos())
				view.getEnvioDialogo().getDialog().open();
		});	
		
		//botao descartar, retorna para página anterior
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
	
	//aqui vai a lógica para salvar a correção e a questão, e enviar
	private void enviarQuestao(ClickEvent<Button> event) {
		QuestaoObjetiva questao = view.getQuestao();
		
		Notification notification;
		
		Correcao correcao = new Correcao();
		
		//chama o método para verificar os dados da view
		//se ele retornou true todos os dados foram coletados com sucesso
		if(verificaDadosPreenchidos()) {
			
			/*
			 * IMPLEMENTAR
			 */
			
			questao.setAlternativaCorreta(correta);
			questao.setAlternativas(alternativasList);
			questao.setEnunciado(enunciado);
			questao.setJustificativa(justificativaCorreta);
			
			correcao.setAtendimentoSugestoes(atendimento);

			questao.enviarParaRevisao(correcao);
				
			//salva a questao
			questaoService.getRepository().save(questao);;
			
			//Notifica ação bem sucedida
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
		enunciado = view.getEnunciado().getValue();
		justificativaCorreta = view.getJustificativaCorretaTA().getValue();
		justificativaAtendimento = view.getJustificativaAtendimentoTA().getValue();
		alternativasList = new ArrayList<String>();
		
		try {
			atendimento = dedicirAtendimento();
		} catch (InvalidAttributeIdentifierException e) {
			e.printStackTrace();
		}
		
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

		//verificando demais campos em branco
		if(enunciado.isEmpty() || justificativaAtendimento.isEmpty() || justificativaCorreta.isEmpty()) {
			notification = Notification
			        .show("Campos em branco." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
		
		
		
		return true;
	}

	private Atendimento dedicirAtendimento() throws InvalidAttributeIdentifierException {
		
		String valor = view.getRadioGroup().getValue();
		
		if(valor.toLowerCase().contains("totalmente")) {
			return Atendimento.TOTAL;
		}
		if(valor.toLowerCase().contains("parcialmente")) {
			return Atendimento.PARCIAL;
		}
		if(valor.toLowerCase().contains("não")) {
			return Atendimento.NAO_ATENDIDA;
		}
		
		throw new InvalidAttributeIdentifierException();
	}
	
}
