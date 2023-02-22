package br.ufg.sep.views.questoes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.state.stateImpl.Elaboracao;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.componente.ConfirmaEnvioRevisaoDialog;
import br.ufg.sep.views.questoes.componente.MetadadosQuestaoComponent;
import br.ufg.sep.views.questoes.presenter.VisualizarQuestaoObjetivaPresenter;

@Route(value="visualizar_questao", layout = MainLayout.class)
@PageTitle("Visualizar Questão Objetiva")
@PermitAll
public class VisualizarQuestaoObjetivaView extends VerticalLayout implements HasUrlParameter<Long>{

	private ProvaService provaService;
	private QuestaoService questaoService;
	private VisualizarQuestaoObjetivaPresenter presenter;
	
	//inputs gerais
	private TextField subareaTF;
	private ComboBox<NivelDificuldade> nivelDificuldadeCombo;
	private TextArea justificativaTA;
	
	//inputs da questao
	private TextArea enunciado;	
	private List<TextArea> alternativasList;
	private List<Checkbox> checkboxList;
	
	private Span span2;

	private Button voltarButton;
	private Button enviarButton;

	//layouts final
	//todos eles são criados no construtor
	//e adicionados posteriormente
	private VerticalLayout layoutFinal; 
	private VerticalLayout alternativaLayout;
	private VerticalLayout justificativaLayout;
	private VerticalLayout buttonsLayout;

	private QuestaoObjetiva questaoObjetiva;
	private long questaoId;

	private MetadadosQuestaoComponent metadados;
	private ConfirmaEnvioRevisaoDialog envioDialogo;
	
	private int quantAlternativas;

	public VisualizarQuestaoObjetivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;

		//criando os layouts intermediarios
		VerticalLayout informacaoLayout = new VerticalLayout();
		VerticalLayout enunciadoLayout = new VerticalLayout();
				
		//criando as listas que serao usadas pra guardar os componentes
		alternativasList = new ArrayList<>();
		checkboxList = new ArrayList<>();		
		
		//criando os layouts principais
		alternativaLayout = new VerticalLayout();
		layoutFinal = new VerticalLayout();
		justificativaLayout = new VerticalLayout();
		buttonsLayout = new VerticalLayout();
		
		//ajustando tamanho dos layouts
		enunciadoLayout.setWidth("667px");
		alternativaLayout.setWidth("700px");
		informacaoLayout.setWidth("700px");
		informacaoLayout.setPadding(false);
		justificativaLayout.setWidth("699px");
		buttonsLayout.setWidth("700px");
		
		metadados = new MetadadosQuestaoComponent();
		envioDialogo = new ConfirmaEnvioRevisaoDialog();
		
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
		
		informacaoLayout.add(statusV, metadados); //adicionando ao layout intermediario
		
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
		
		layoutFinal.add(informacaoLayout, enunciadoLayout);
		
		add(layoutFinal);
		
	}

	private void addAlternativas() {
	
		//Layout de Label
		Label alternativaLabel = new Label("Alternativas");
		Label corretaLabel = new Label("Correta");
			
		HorizontalLayout labelLayout = new HorizontalLayout();
		
		alternativaLabel.setWidth("597px");
		labelLayout.add(alternativaLabel, corretaLabel);
		
		
		//lista de span que guarda as alternativas a), b)...
		List<Span> spanList = new ArrayList<Span>();
		
		
		//layout auxiliar para centralizar os checkbox
		List<HorizontalLayout> auxLayout = new ArrayList<HorizontalLayout>();
				
		//layout para guardar a letra, textArea e Checkbox (por alternativa)
		List<HorizontalLayout> altLayout = new ArrayList<HorizontalLayout>();
		
		//adicionnado o layout de labels
		alternativaLayout.add(labelLayout);
						
		//para popular a lista de Span com as letras das alternativas
		char a = 'a';
		for(int i = 0; i < quantAlternativas; i++) {
			spanList.add(new Span(a + ")"));
			a++;
		}	
		
		//criando os layout das alternativas
		//contem a letra "a)" o textArea e o checkbox da alternativa
		for(int i = 0; i < quantAlternativas; i++) {
			
			//cria o textarea e adiciona ao list 
			alternativasList.add(new TextArea());
			alternativasList.get(i).setWidth("570px");
			
			//setando o valor da alternativa
			alternativasList.get(i).setValue(questaoObjetiva.getAlternativas().get(i));
			alternativasList.get(i).setReadOnly(true);
			
			//cria o textbox e adiciona ao list
			checkboxList.add(new Checkbox());
			checkboxList.get(i).setReadOnly(true);
			
			if(i == questaoObjetiva.getAlternativaCorreta())
				checkboxList.get(i).setValue(true);
			
			//layout para organizar o checkbox
			auxLayout.add(new HorizontalLayout());
			auxLayout.get(i).setPadding(true);
			auxLayout.get(i).add(checkboxList.get(i));
			
			altLayout.add(new HorizontalLayout());
			altLayout.get(i).setAlignItems(Alignment.CENTER);
			altLayout.get(i).add(spanList.get(i), alternativasList.get(i), auxLayout.get(i));
		}
				
		//adicionando os layouts de alternativas individuais ao layout geral
		for(int i = 0; i <quantAlternativas; i++) {	
			alternativaLayout.add(altLayout.get(i));
		}

		this.add(alternativaLayout);
	}

	private void addJustificativa() {
		
		Label justificativaLabel = new Label("Justificativa da alternativa correta:");
		
		justificativaTA = new TextArea();
		justificativaTA.setWidthFull();
		
		justificativaTA.setValue(questaoObjetiva.getJustificativa());
		justificativaTA.setReadOnly(true);
		
		justificativaLayout.add(justificativaLabel, justificativaTA);
		
		justificativaLayout.setSpacing(false);
		
		add(justificativaLayout);
	}
	
	private void addBotões() {
		//critando botoes
		
		HorizontalLayout h = new HorizontalLayout();
		
		voltarButton = new Button("Voltar");
		this.enviarButton = new Button("Enviar para revisão 1");
		
		voltarButton.getStyle().set("margin-right", "auto");
		
		enviarButton.getStyle().set("margin-left", "405px");
		h.add(voltarButton,enviarButton);
		
		buttonsLayout.add(h);
		
		this.add(buttonsLayout);
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questaoObjetiva = (QuestaoObjetiva) optionalQuestao.get();	
			
			quantAlternativas = questaoObjetiva.getQuantAlternativas();
			questaoId = questaoObjetiva.getId();
			//setando o valor dos campos criados no construtor
			//pode virar uma funcao mas fiquei com preguica
			enunciado.setValue(questaoObjetiva.getEnunciado());
			
			//setando os valores do componente de metadados
			metadados.setSubAreas(questaoObjetiva.getSubAreas());
			metadados.atualizaGrid(); //atualizando o grid apos setar a lista de subareas
			metadados.setEdicaoFalse(); //desabilitando a edicao dos componentes
			metadados.getNivelDificuldadeCombo().setValue(questaoObjetiva.getNivelDificuldade());
			span2.setText("" + questaoObjetiva.getState().toString());
			
			//chama o método que adiciona o layout de alternativas de acordo com a quantidade de questoes
			//definidas no cadastro da prova
			addAlternativas();

			//chama o método para criar o resto do layout
			addJustificativa();
			addBotões();
			
			//se nao tiver em elaboração, desabilitar o botao enviar
			if(!(questaoObjetiva.getState() instanceof Elaboracao)) {
				this.enviarButton.setEnabled(false);
			}

			this.presenter = new VisualizarQuestaoObjetivaPresenter(provaService, questaoService, this); //iniciar o presenter
		}
		
		else {
			Notification notification = Notification
			        .show("Impossível acessar a questão");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}
	
	public Button getEnviarButton() {
		return enviarButton;
	}
	
	public Button getVoltarButton() {
		return voltarButton;
	}
	
	public Questao getQuestaoObjetiva() {
		return questaoObjetiva;
	}
	
	public long getQuestaoId() {
		return questaoId;
	}
	
	public ConfirmaEnvioRevisaoDialog getEnvioDialogo() {
		return envioDialogo;
	}
	
}
