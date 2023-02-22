package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.state.stateImpl.Elaboracao;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.componente.ConfirmaEnvioRevisaoDialog;
import br.ufg.sep.views.questoes.componente.MetadadosQuestaoComponent;
import br.ufg.sep.views.questoes.presenter.NovaQuestaoDiscursivaPresenter;
import br.ufg.sep.views.questoes.presenter.VisualizarQuestaoDiscursivaPresenter;

@Route(value="visualizar_questoes_discursiva", layout = MainLayout.class)
@PageTitle("Visualizar Questão Discursiva")
@PermitAll
public class VisualizarQuestaoDiscursivaView extends VerticalLayout implements HasUrlParameter<Long>{

	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private VisualizarQuestaoDiscursivaPresenter presenter;
	
	private MetadadosQuestaoComponent metadados;
	private ConfirmaEnvioRevisaoDialog envioDialogo;
	
	private Span span2;
	//layouts final
	//todos eles são criados no construtor
	//e adicionados posteriormente
	private VerticalLayout layoutFinal; 
	private VerticalLayout buttonsLayout;
	
	//inputs da questao
	private TextArea enunciado;	
	private TextArea respostaEsperada;	
	
	private Button voltarButton;
	private Button enviarButton;
	
	private Prova prova;
	private QuestaoDiscursiva questaoDiscursiva;
	
	public Button getVoltarButton() {
		return voltarButton;
	}
	public Button getEnviarButton() {
		return enviarButton;
	}
	public QuestaoDiscursiva getQuestaoDiscursiva() {
		return questaoDiscursiva;
	}
	public VisualizarQuestaoDiscursivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		metadados = new MetadadosQuestaoComponent();
		envioDialogo = new ConfirmaEnvioRevisaoDialog();
		
		//criando os layouts intermediarios
		VerticalLayout informacaoLayout = new VerticalLayout();
		VerticalLayout enunciadoLayout = new VerticalLayout();
		VerticalLayout respostaEsperadaLayout = new VerticalLayout();
		
		//criando os layouts principais
		layoutFinal = new VerticalLayout();
		buttonsLayout = new VerticalLayout();
		
		//ajustando tamanho dos layouts
		enunciadoLayout.setWidth("667px");
		informacaoLayout.setWidth("700px");
		informacaoLayout.setPadding(false);
		informacaoLayout.getStyle().set("margin-bottom", "15px");
		
		respostaEsperadaLayout.setWidth("667px");
		buttonsLayout.setWidth("685px");
		
		HorizontalLayout statusH = new HorizontalLayout();
		VerticalLayout statusV = new VerticalLayout();
		
		Span span1 = new Span("Status:");
		span2 = new Span();
		span1.getStyle().set("font-weight", "bold");
		span2.getStyle().set("font-weight", "bold");
		
		statusH.add(span1, span2);
		statusV.setAlignItems(Alignment.CENTER);
		statusV.add(statusH);
		//statusV.setPadding(false);
		statusV.setWidth("665px");
		statusV.getStyle().set("border", "1px solid lightsteelblue");
		
		informacaoLayout.add(statusV, metadados);
		
		/**************** Layout do enunciado ***********************/
		
		//criando os componentes do layout de questao
		Label enunciadoLabel = new Label("Enunciado");
		enunciado = new TextArea();
		
		//alterando estilos
		enunciado.setWidthFull();
		enunciado.setMinHeight("150px");
		enunciado.setReadOnly(true);

		enunciadoLayout.add(enunciadoLabel, enunciado);
		
		//estilo do layout do enunciado
		enunciadoLayout.setSpacing(false);
		enunciadoLayout.setPadding(false);
		
		/**************** Layout da resposta esperada ***********************/
		
		//criando os componentes
		Label labelRespEsperada = new Label("Resposta Esperada");
		respostaEsperada = new TextArea();
		
		//alterando estilos
		respostaEsperada.setWidthFull();
		respostaEsperada.setMinHeight("150px");
		respostaEsperada.setReadOnly(true);
		
		respostaEsperadaLayout.add(labelRespEsperada, respostaEsperada);
		
		//estilo do layout do enunciado
		respostaEsperadaLayout.setSpacing(false);
		respostaEsperadaLayout.setPadding(false);
		
		addBotoes();
		
		layoutFinal.add(informacaoLayout, enunciadoLayout, respostaEsperadaLayout, buttonsLayout);
		
		add(layoutFinal);
	}
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questaoDiscursiva = (QuestaoDiscursiva) optionalQuestao.get();	
			
			setarDados();
			
			presenter = new VisualizarQuestaoDiscursivaPresenter(provaService, questaoService, this);
		} else {
			Notification notification = Notification
			        .show("Impossível acessar a questão");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}

	private void setarDados() {
		// TODO Auto-generated method stub
		//setando os metadados
		this.metadados.setEdicaoFalse();
		this.metadados.getNivelDificuldadeCombo().setValue(questaoDiscursiva.getNivelDificuldade());
		this.metadados.setSubAreas(questaoDiscursiva.getSubAreas());
		this.metadados.atualizaGrid();
		
		span2.setText("" + questaoDiscursiva.getState().toString());
		
		//setando as informações das questões
		this.enunciado.setValue(questaoDiscursiva.getEnunciado());
		this.respostaEsperada.setValue(questaoDiscursiva.getRespostaEsperada());
		
		//se nao tiver em elaboração, desabilitar o botao enviar
		if(!(questaoDiscursiva.getState() instanceof Elaboracao)) {
			this.enviarButton.setEnabled(false);
		}
		
	}
	private void addBotoes() {
		//critando botoes
		
		HorizontalLayout h1 = new HorizontalLayout();
		
		voltarButton = new Button("Voltar");
		voltarButton.getStyle().set("margin-right", "auto");
		
		//este botão poderá sofrer mudanças de acordo com o status da questão
		enviarButton = new Button("Enviar para Revisão Técnica 1");
		enviarButton.getStyle().set("margin-left", "350px");
		
		h1.add(voltarButton, enviarButton);

		buttonsLayout.setPadding(false);
		buttonsLayout.add(h1);
	}
	
	public ConfirmaEnvioRevisaoDialog getEnvioDialogo() {
		return envioDialogo;
	}
}
