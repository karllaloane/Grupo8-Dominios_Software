package br.ufg.sep.views.correcao.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributeIdentifierException;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Atendimento;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.correcao.CorrecaoDiscursivaBancaView;
import br.ufg.sep.views.questoes.QuestoesProvaView;

public class CorrecaoDiscursivaBancaPresenter {
	private ProvaService provaService;
	private QuestaoService questaoService;
	private CorrecaoDiscursivaBancaView view;
	
	private int correta;
	
	//variáveis para coletar os dados da view
	private String enunciado;
	private String respostaEsperada;
	private String justificativaAtendimento;
	private Atendimento atendimento;
	
	private Prova prova; 
	
	public CorrecaoDiscursivaBancaPresenter(ProvaService provaService, QuestaoService questaoService,
			CorrecaoDiscursivaBancaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		prova = view.getProva();
	
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
	
		QuestaoDiscursiva questao = view.getQuestao();
		Correcao correcao = new Correcao();
		
		Notification notification;
		
		//chama o método para verificar os dados da view
		//se ele retornou true todos os dados foram coletados com sucesso
		if(verificaDadosPreenchidos()) {
		
			/*
			 * IMPLEMENTAR
			 */
			
			questao.setEnunciado(enunciado);
			questao.setRespostaEsperada(respostaEsperada);
			
			correcao.setAtendimentoSugestoes(atendimento);

			correcao.setJustificativaDoAtendimento(justificativaAtendimento);

			
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
		respostaEsperada = view.getRespostaEsperadaTA().getValue();
		justificativaAtendimento = view.getJustificativaAtendimentoTA().getValue();
	
		try {
			atendimento = dedicirAtendimento();
		} catch (InvalidAttributeIdentifierException e) {
			e.printStackTrace();
		}
	
		//pra nao deixar salvar sem marcar a questao correta
		if(correta == -1) {
			notification = Notification
			        .show("Selecione a alternativa correta.");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return false;
		}
	
		//verificando demais campos em branco
		if(enunciado.isEmpty() || justificativaAtendimento.isEmpty() || respostaEsperada.isEmpty()) {
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
