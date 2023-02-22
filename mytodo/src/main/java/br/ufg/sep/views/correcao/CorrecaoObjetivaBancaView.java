package br.ufg.sep.views.correcao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import br.ufg.sep.views.revisao.components.DropDownQuestaoFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Atendimento;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.state.stateImpl.Correcao1;
import br.ufg.sep.state.stateImpl.Correcao2;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.correcao.CorrecaoDiscursivaBancaView.Data;
import br.ufg.sep.views.correcao.presenter.CorrecaoObjetivaBancaPresenter;
import br.ufg.sep.views.questoes.componente.CancelarEdicaoDialog;
import br.ufg.sep.views.questoes.componente.ConfirmaEnvioRevisaoDialog;

@Route(value="correcao_questao_objetiva", layout = MainLayout.class)
@PageTitle("Correção da Banca")
@PermitAll
public class CorrecaoObjetivaBancaView extends VerticalLayout implements HasUrlParameter<Long>{

	private VerticalLayout revisaoTecnicaLayout;
	private VerticalLayout revisaoBancaLayout;
	private VerticalLayout questaoLayout;
	private HorizontalLayout gridL;
	private TextArea orientacoesTextField;
	private TextArea justificativaAtendimentoTA;
	private TextArea justificativaCorretaTA;
	private RadioButtonGroup<String> radioGroup;
	
	private List<String> topicosDeRevisao;
	
	//inputs da questao
	private TextArea enunciado;	
	private List<TextArea> alternativasList;
	private List<Checkbox> checkboxList;
	
	private Button salvarButton;
	private Button descartarButton;
	private Button enviarButton;
	
	private ConfirmaEnvioRevisaoDialog envioDialogo;
	private CancelarEdicaoDialog cancelarDialogo;
	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private CorrecaoObjetivaBancaPresenter presenter;
	
	private QuestaoObjetiva questao;
	private Prova prova;
	private int quantAlternativas;

	
	public CorrecaoObjetivaBancaView(ProvaService provaService, QuestaoService questaoService) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		envioDialogo = new ConfirmaEnvioRevisaoDialog();
		cancelarDialogo = new CancelarEdicaoDialog();
		
		add();
		
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questao = (QuestaoObjetiva) optionalQuestao.get();
			
			prova = questao.getProva();
			
			quantAlternativas = questao.getQuantAlternativas();
			
			criaLayoutRevisaoTecnica();
			criaLayoutRevisaoBanca();
			criaLayoutQuestao();
			criaBotoesLayout();
			
			presenter = new CorrecaoObjetivaBancaPresenter(provaService, questaoService, this);
			
		} else {
			Notification notification = Notification
			        .show("Impossível acessar a questão");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}
	
	private void criaLayoutQuestao() {
		questaoLayout = new VerticalLayout();
		
		Span spanQuestao = new Span("Questão Reelaborada");
		spanQuestao.getStyle().set("font-size", "16px").set("font-weight", "bold");
				
		VerticalLayout enunciadoLayout = new VerticalLayout();
		
		//criando os componentes do layout de questao
		Span enunciadoSpan = new Span("Enunciado");
		enunciado = new TextArea();
		enunciado.setValue(questao.getEnunciado());
		
		//alterando estilos
		enunciado.setWidthFull();
		enunciado.setMinHeight("150px");

		enunciadoLayout.setPadding(false);
		enunciadoLayout.setSpacing(false);
		enunciadoLayout.add(enunciadoSpan, enunciado);
		
		questaoLayout.setWidth("800px");
		questaoLayout.setAlignItems(Alignment.CENTER);
		
		questaoLayout.add(spanQuestao, enunciadoLayout);
		
		this.add(questaoLayout);
		criaAlternativasLayout();
		criaJustificativaLayout();		
	}
	
	private void criaJustificativaLayout() {
		VerticalLayout justificativaLayout = new VerticalLayout();
		
		Span justificativaSpan = new Span("Justificativa da alternativa correta:");
		
		justificativaCorretaTA = new TextArea();
		justificativaCorretaTA.setWidthFull();
		justificativaCorretaTA.setValue(questao.getJustificativa());
		
		justificativaLayout.setWidth("800px");
		justificativaLayout.add(justificativaSpan, justificativaCorretaTA);
		
		justificativaLayout.setSpacing(false);
		
		this.add(justificativaLayout);	
	}

	private void criaAlternativasLayout() {
		VerticalLayout alternativaLayout = new VerticalLayout();
		
		//criando as listas que serao usadas pra guardar os componentes
		alternativasList = new ArrayList<>();
		checkboxList = new ArrayList<>();
		
		//Layout de Label
		Label alternativaLabel = new Label("Alternativas");
		Label corretaLabel = new Label("Correta");
			
		HorizontalLayout labelLayout = new HorizontalLayout();
		
		alternativaLabel.setWidth("680px");
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
			alternativasList.get(i).setWidth("650px");
			alternativasList.get(i).setValue(questao.getAlternativas().get(i));
			
			//cria o textbox e adiciona ao list
			checkboxList.add(new Checkbox());
			
			if(i == questao.getAlternativaCorreta())
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

	private void criaBotoesLayout() {
		//critando botoes
		VerticalLayout buttonsLayout = new VerticalLayout();
		buttonsLayout.setWidth("800px");
		
		HorizontalLayout h = new HorizontalLayout();
		
		this.descartarButton = new Button("Descartar edição");
		this.enviarButton = new Button("Enviar para revisão técnica");
		
		this.enviarButton.getStyle().set("margin-left", "393px");
		h.add(descartarButton,enviarButton);
		
		buttonsLayout.add(h);
		
		this.add(buttonsLayout);
	}
	
	private void criaLayoutRevisaoTecnica() {
		
		revisaoTecnicaLayout = new VerticalLayout();
		
		VerticalLayout layoutGrid = new VerticalLayout();
		VerticalLayout orientGrid = new VerticalLayout();
		
		Span revSpan = new Span("");
		
		revSpan.getStyle().set("font-size", "16px").set("font-weight", "bold");
		
		if(questao.getState() instanceof Correcao1)
			revSpan.setText("Dados da Revisão Técnica 1");
		else if(questao.getState() instanceof Correcao2) 
			revSpan.setText("Dados da Revisão Técnica 2");
		
		Span orientacaoSpan = new Span("Orientações");
		orientacoesTextField = new TextArea();
		orientacoesTextField.setValue( questao.getState().getRevisao().getOrientacoes());
		orientacoesTextField.setWidthFull();
		orientacoesTextField.setReadOnly(true);
		
		Span itensSpan = new Span("Itens Avaliados");
		criarGrid();
		
		orientGrid.add(itensSpan, gridL);
		layoutGrid.add(orientacaoSpan, orientacoesTextField);
		
		//orientGrid.setSpacing(false);
		orientGrid.setPadding(false);
		orientGrid.getStyle().set("margin-bottom", "10px");
		//layoutGrid.setSpacing(false);
		layoutGrid.setPadding(false);
		
		revisaoTecnicaLayout.setWidth("760px");
		revisaoTecnicaLayout.setAlignItems(Alignment.CENTER);
		DropDownQuestaoFactory dropDownQuestaoFactory = new DropDownQuestaoFactory(this.questao);
		dropDownQuestaoFactory.tituloSumario.setText("Questão avaliada");
		this.add(dropDownQuestaoFactory.getComponent());
		revisaoTecnicaLayout.add(revSpan, orientGrid, layoutGrid);


		this.add(revisaoTecnicaLayout);

		Details details = new Details(revSpan, revisaoTecnicaLayout);
		details.addThemeVariants(DetailsVariant.FILLED);
		details.setWidth("785px");
		details.setMinWidth("700px");
		details.setOpened(true);
		
		this.add(details);

	}
	
	private void criaLayoutRevisaoBanca() {
		revisaoBancaLayout = new VerticalLayout();
		
		radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("Atendidas totalmente", "Atendidas Parcialmente", "Não Atendidas");
        radioGroup.setValue("Atendidas totalmente");
		
		HorizontalLayout radioGrid = new HorizontalLayout();
		VerticalLayout justificativaGrid = new VerticalLayout();
		
		//radioGrid.setPadding(true);
		radioGrid.setWidth("800px");
		radioGrid.setAlignItems(Alignment.CENTER);
		radioGrid.getStyle().set("margin-bottom", "10px").set("margin-left", "30px");
		justificativaGrid.setSpacing(false);
		justificativaGrid.setPadding(false);
		
		Span revSpan = new Span("Revisão da Banca");
		revSpan.getStyle().set("font-size", "16px").set("font-weight", "bold");
		
		Span radioSpan = new Span("As sugestões foram:");
		radioSpan.getStyle().set("margin-right", "5px");
		radioGrid.add(radioSpan, radioGroup);
		
		Span justSpan = new Span("Justificativa de atendimento");
		//justSpan.getStyle().set("font-weight", "bold");
		
		justificativaAtendimentoTA = new TextArea();
		justificativaAtendimentoTA.setWidthFull();
		
		justificativaGrid.add(justSpan, justificativaAtendimentoTA);
		
		revisaoBancaLayout.add(revSpan, radioGrid, justificativaGrid);
		revisaoBancaLayout.setWidth("800px");
		revisaoBancaLayout.setAlignItems(Alignment.CENTER);
		revisaoBancaLayout.setPadding(true);
		
		this.add(revisaoBancaLayout);
        
	}
	
	private void criarGrid(){
		gridL = new HorizontalLayout();
		gridL.setWidth("727px");
		
		Grid<Data> grid = new Grid();
		
		List<Data> list = new ArrayList<>();
		
		grid.setItems(list);
		
		for(Map.Entry<String, Atendimento> pair : questao.getState().getRevisao().getItemAnalisado().entrySet()){
			list.add(new Data(pair.getKey(), pair.getValue().toString()));
		}
		
		grid.addColumn(Data::getCriterio).setHeader("Critério");
		grid.addColumn(Data::getAtendimento).setHeader("Atendimento");

		grid.addThemeVariants(GridVariant.LUMO_COMPACT);
		grid.setAllRowsVisible(true);
		
		gridL.setSpacing(false);
		gridL.add(grid);
	}
	
	public class Data {
		String criterio;
		String atendimento;
		
		public Data(String criterio, String string){
			this.criterio = criterio;
			this.atendimento = string;
		}
		
		String getCriterio() {
			return criterio;
		}
		
		String getAtendimento() {
			return atendimento;
		}
	}

	
	public Prova getProva() {
		return prova;
	}

	public TextArea getOrientacoesTextField() {
		return orientacoesTextField;
	}

	public TextArea getJustificativaAtendimentoTA() {
		return justificativaAtendimentoTA;
	}

	public TextArea getJustificativaCorretaTA() {
		return justificativaCorretaTA;
	}

	public RadioButtonGroup<String> getRadioGroup() {
		return radioGroup;
	}

	public TextArea getEnunciado() {
		return enunciado;
	}

	public List<TextArea> getAlternativasList() {
		return alternativasList;
	}

	public List<Checkbox> getCheckboxList() {
		return checkboxList;
	}

	public Button getSalvarButton() {
		return salvarButton;
	}

	public Button getDescartarButton() {
		return descartarButton;
	}

	public Button getEnviarButton() {
		return enviarButton;
	}

	public ConfirmaEnvioRevisaoDialog getEnvioDialogo() {
		return envioDialogo;
	}

	public CancelarEdicaoDialog getCancelarDialogo() {
		return cancelarDialogo;
	}

	public QuestaoObjetiva getQuestao() {
		return questao;
	}

}
