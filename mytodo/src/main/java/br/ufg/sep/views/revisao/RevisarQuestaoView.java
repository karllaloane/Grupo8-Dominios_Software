package br.ufg.sep.views.revisao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import br.ufg.sep.entity.*;
import br.ufg.sep.state.stateImpl.Revisao2;
import br.ufg.sep.state.stateImpl.Revisao3;
import br.ufg.sep.views.revisao.components.DropDownQuestaoFactory;
import br.ufg.sep.views.revisao.presenter.RevisarQuestaoPresenter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.correcao.presenter.CorrecaoObjetivaBancaPresenter;

@Route(value="revisar-questao", layout = MainLayout.class)
@PageTitle("Revisar")
@PermitAll

public class RevisarQuestaoView extends HorizontalLayout  implements HasUrlParameter<Long> {

	/* Inputs do concurso */
	private TextField nomeConcurso = new TextField("Nome", "", "");
	private TextField cidadeConcurso = new TextField("Cidade", "", "");
	private DatePicker dataInicioConcurso = new DatePicker("Data de início");
	private DatePicker dataFimConcurso = new DatePicker("Data do fim");
	
	/* Inputs da prova */
	private TextField areaConhecimento = new TextField("Área de conhecimento", "", "");
    private TextField tipoProva = new TextField("Tipo de Prova", "", "");
    private TextField numAlternativas = new TextField("Número de Alternativas ", "", "");
    private TextField nivelProva = new TextField("Nível da Prova", "", "");
    private DatePicker prazo = new DatePicker("Prazo");
    private TextArea descricaoDaProva = new TextArea("Descriçãop da Prova", "", "");
    private Button baixarAnexo = new Button("Baixar Anexo"); 
    
    /* Inputs da questão */
    private TextArea subAreasQuestao = new TextArea("Sub-áreas da questão ", "", "");
    private TextArea enunciadoQuestao = new TextArea("Enunciado", "", "");
	private TextField nivelDificuldadeQuestaoCombo = new TextField("Nível de Dificuldade", "", "");
	private TextArea justificativaQuestao = new TextArea("Justificativa da Alternativa Correta", "", "");
	private TextArea alternativaAQuestao = new TextArea("A) ", "", "");
	private TextArea alternativaBQuestao = new TextArea("B) ", "", "");
	private TextArea alternativaCQuestao = new TextArea("C) ", "", "");
	private TextArea alternativaDQuestao = new TextArea("D) ", "", "");
	private TextArea alternativaEQuestao = new TextArea("E) ", "", "");
	private TextArea orientacoesQuestao = new TextArea("Orientações da Revisão", "", "");
	
	/* Imputs de revisão */
	private TextArea observacaoRevisao = new TextArea("Observação da última Revisão ", "", "");

	private TextArea estadoQuestao = new TextArea("Estado da questão");

	/* Imputs gerais */
	private Button enviarBanca = new Button("Desaprovar questão", new Icon(VaadinIcon.CLOSE_CIRCLE));
	private Button enviarRevisao = new Button("Aprovar questão", new Icon(VaadinIcon.CHECK_CIRCLE));
	private ProvaService provaService;
	private Prova prova;
	private int quantAlternativas;
	private QuestaoService questaoService;
	VerticalLayout verticalDetails = new VerticalLayout();
	VerticalLayout layoutQuestao = new VerticalLayout();
	VerticalLayout layoutgrid = new VerticalLayout();
	HorizontalLayout botoesLayout = new HorizontalLayout(enviarBanca, enviarRevisao);
	private Details detailsConcurso;
	private Details detailsProva;
	private Details detailsQuestaoAtual;
	private Details detailsUltimaRevisao;
	private TextField topicosDeRevisaoTF;
	private Button adicionarButton;
    private Grid<String> topicosAnalisadosGrid;
    private HorizontalLayout gridL = new HorizontalLayout();
    private CorrecaoObjetivaBancaPresenter presenter;
    private VerticalLayout revisaoTecnicaLayout = new VerticalLayout();
    private TextArea orientacoesTextField = new TextArea();
    private QuestaoObjetiva questaoObjetiva;

	private Button addButton;
	private VerticalLayout topicosDeRevisaoLayout;
	private HorizontalLayout addtopicosDeRevisaoLayout;
	private List<String> topicosDeRevisao;

	private HashMap<String, Atendimento> topicosAnalisadosHashMap = new HashMap<>();

	private Questao questaoSelecionada;

	private RevisarQuestaoPresenter revisarQuestaoPresenter;
	QuestaoDiscursiva questaoDiscussiva;

	public RevisarQuestaoView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		topicosAnalisadosGrid = new Grid<>();

		add();
	}
	
	private void dropMenuRevisão() {
		revisaoTecnicaLayout = new VerticalLayout();
		
		VerticalLayout layoutGrid = new VerticalLayout();
		VerticalLayout orientGrid = new VerticalLayout();
		
		Span revSpan = new Span("");
		
		revSpan.setText("Informações da última Revisão");
		
		
		Span orientacaoSpan = new Span("Orientações");
		orientacoesTextField = new TextArea();
		if(questaoObjetiva!=null) {
			if (questaoObjetiva.getState().getRevisao() != null)
				orientacoesTextField.setValue(questaoObjetiva.getState().getRevisao().getOrientacoes());
		}
		else {
			if(questaoDiscussiva.getState().getRevisao()!=null)
			orientacoesTextField.setValue(questaoDiscussiva.getState().getRevisao().getOrientacoes());
		}
		orientacoesTextField.setWidthFull();
		orientacoesTextField.setReadOnly(true);
		
		Span itensSpan = new Span("Itens Avaliados");
		criarGrid();
		
		orientGrid.add(itensSpan, gridL);
		layoutGrid.add(orientacaoSpan, orientacoesTextField);
		
		orientGrid.setPadding(false);
		orientGrid.getStyle().set("margin-bottom", "10px");

		layoutGrid.setPadding(false);
		
		revisaoTecnicaLayout.add(revSpan, orientGrid, layoutGrid);

		this.add(revisaoTecnicaLayout);

		detailsUltimaRevisao = new Details(revSpan, revisaoTecnicaLayout);
		detailsUltimaRevisao.addThemeVariants(DetailsVariant.FILLED);
		detailsUltimaRevisao.setWidth("1070px");
		detailsUltimaRevisao.setOpened(true);
	}
	
	private void criarGrid(){
		gridL = new HorizontalLayout();
		gridL.setWidth("1000px");
		
		Grid<Data> grid = new Grid();
		
		List<Data> list = new ArrayList<>();
		
		grid.setItems(list);
		if(questaoObjetiva!=null)
		if(questaoObjetiva.getState().getRevisao()!=null)
		for(Map.Entry<String, Atendimento> pair : questaoObjetiva.getState().getRevisao().getItemAnalisado().entrySet()){
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
	
	
	public void campoObservacao() {
		orientacoesQuestao.setWidth("1030px");
	}
	
	public void topicosDeRevisaoComponent(){
		/* Arraylist de tópicos */ 
		topicosDeRevisao = new ArrayList<>();
		
		/* Formatando a tabela*/
		addtopicosDeRevisaoLayout = new HorizontalLayout();
		addtopicosDeRevisaoLayout.setPadding(false);
		topicosDeRevisaoLayout = new VerticalLayout();
		topicosDeRevisaoLayout.setPadding(false);
		addtopicosDeRevisaoLayout.setWidthFull();
		topicosDeRevisaoLayout.setWidth("1030px");
	
		topicosDeRevisaoTF = new TextField();
		topicosDeRevisaoTF.setTooltipText("Informe o Tópico a ser avaliado");
		topicosDeRevisaoTF.setWidth("1030px");
		
		addButton = new Button("Adicionar");
		addButton.setWidth("200px");

		addtopicosDeRevisaoLayout.add(topicosDeRevisaoTF, addButton);
		
		topicosAnalisadosGrid.setWidth("1200px");
		
		topicosDeRevisaoLayout.add(new Span("Tópicos avaliados na Revisão"), topicosAnalisadosGrid, addtopicosDeRevisaoLayout);
		
		topicosAnalisadosGrid.setItems(topicosDeRevisao);
		topicosAnalisadosGrid.setAllRowsVisible(true);
		
		//adicionando a coluna
		//conterá o arraylist e o botão de remover
		topicosAnalisadosGrid.addColumn(item -> item).setFlexGrow(1);
		/*
		topicosAnalisadosGrid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkAtende, item) -> {
                	checkAtende.setLabel("Atende");
                })).setWidth("130px").setFlexGrow(0).setKey("atende");

		topicosAnalisadosGrid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkAtendeParcialmente, item) -> {
					Checkbox checkbox = new Checkbox();
                	checkAtendeParcialmente.setLabel("Atende Parcialmente");
                	// Ajeitar o taamanho disso aqui
                })).setWidth("220px").setFlexGrow(0).setKey("atende-parcialmente");
		
		topicosAnalisadosGrid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkNaoAtende, item) -> {
                	checkNaoAtende.setLabel("Não Atende");
                })).setWidth("130px").setFlexGrow(0).setKey("nao-atende");
		*/

		topicosAnalisadosGrid.setSelectionMode(Grid.SelectionMode.NONE);
		topicosAnalisadosGrid.addColumn(
				new ComponentRenderer<>( topico->{
					RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
					radioGroup.setItems(
							Atendimento.TOTAL.toString(),
							Atendimento.PARCIAL.toString(),
							Atendimento.NAO_ATENDIDA.toString()
					);

					this.topicosAnalisadosHashMap.forEach((topic, atend)->{
						if(topico.equals(topic))
							radioGroup.setValue(atend.toString());
					});

					radioGroup.addValueChangeListener(v->{
						if(v.getValue().equals(Atendimento.TOTAL.toString())) {
							this.topicosAnalisadosHashMap.remove(topico);
							this.topicosAnalisadosHashMap.put(topico, Atendimento.TOTAL);
							//return;
						}
						if(v.getValue().equals(Atendimento.PARCIAL.toString())) {
							this.topicosAnalisadosHashMap.remove(topico);
							this.topicosAnalisadosHashMap.put(topico, Atendimento.PARCIAL);
							//return;
						}
						if(v.getValue().equals(Atendimento.NAO_ATENDIDA.toString())) {
							this.topicosAnalisadosHashMap.remove(topico);
							this.topicosAnalisadosHashMap.put(topico, Atendimento.NAO_ATENDIDA);
						}
						/*
						System.out.println("|*********************|");
						this.topicosAnalisadosHashMap.forEach((key,obj)->{
							System.out.println(key + " | "+ obj.toString());
						});
						System.out.println("|*********************|");
						*/
					});


					return radioGroup;
				}
				)).setKey("radio-group");

		topicosAnalisadosGrid.addColumn(
				new ComponentRenderer<>(Button::new, (button, item) -> {
					button.addThemeVariants(ButtonVariant.LUMO_ICON,
							ButtonVariant.LUMO_TERTIARY);
					button.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE_O));
					button.addClickListener(e -> this.removeSubArea(item));
				})).setWidth("130px").setFlexGrow(0).setKey("remover");


		// listener pra adicionar os topicos no grid
		addButton.addClickListener(e ->{
			if(!this.topicosDeRevisaoTF.isEmpty()) {
				if(topicosDeRevisao.contains(topicosDeRevisaoTF.getValue())) {
					Notification notification = new Notification("Tópico já adicionado");
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
					notification.setPosition(Notification.Position.TOP_CENTER);
					notification.setDuration(2);
					notification.open();
					return;
				}else
					this.topicosDeRevisao.add(topicosDeRevisaoTF.getValue());
			}
			topicosDeRevisaoTF.clear();
			atualizaGrid();
		});
		
		if(topicosDeRevisao.size() == 0) {
			topicosAnalisadosGrid.setVisible(false);
		} else {
			topicosAnalisadosGrid.setVisible(true);
		}
		
		this.setPadding(false);
	}
	
	//atualizar o grid
	public void atualizaGrid() {
		if(topicosDeRevisao.size() == 0) {
			topicosAnalisadosGrid.setVisible(false);
		} else {

			//topicosAnalisadosGrid.setItems(this.topicosDeRevisao);
			topicosAnalisadosGrid.getDataProvider().refreshAll();
			topicosAnalisadosGrid.setVisible(true);
		}
	}

	//metodo pra remover uma subarea da lista
	private void removeSubArea(String item) {
		topicosDeRevisao.remove(item);
		this.topicosAnalisadosHashMap.remove(item);
		atualizaGrid();
	}
	
	//pra poder setar a edicao falta quando 
	//for apenas visualização
	public void setEdicaoFalse() {
		this.addButton.setVisible(false);
		this.topicosDeRevisaoTF.setVisible(false);
		this.addtopicosDeRevisaoLayout.setVisible(false);
		topicosAnalisadosGrid.getColumnByKey("remover").setVisible(false);;
	}
	
	private void dropMenuConcurso() {
		HorizontalLayout summary = new HorizontalLayout();
		summary.setSpacing(false);
		summary.add(new Text("Concurso - Informações Gerais"));
		
		/* Formatando os campos */
		nomeConcurso.setWidth("700px");
		cidadeConcurso.setWidth("700px");
		dataInicioConcurso.setWidth("300px");
		dataFimConcurso.setWidth("300px");
		
		/* Deixando campos não editáveis */
		nomeConcurso.setReadOnly(true);
		cidadeConcurso.setReadOnly(true);
		dataInicioConcurso.setReadOnly(true);
		dataFimConcurso.setReadOnly(true);
		
		/* Layout final do concurso */
		HorizontalLayout horizontalLayout1 = new HorizontalLayout();
		horizontalLayout1.add(nomeConcurso, dataInicioConcurso);
		HorizontalLayout horizontalLayout2 = new HorizontalLayout();
		horizontalLayout2.add(cidadeConcurso, dataFimConcurso);
		VerticalLayout infosForm = new VerticalLayout();
		infosForm.add(horizontalLayout1, horizontalLayout2);
		
		/* Drop menu*/ 
		detailsConcurso = new Details(summary, infosForm);
		detailsConcurso.addThemeVariants(DetailsVariant.FILLED);
		detailsConcurso.setWidthFull();
		detailsConcurso.setMinWidth("1070px");
		detailsConcurso.setOpened(false);
	}
	
	private void dropMenuProva() {
		HorizontalLayout summary = new HorizontalLayout();
		summary.setSpacing(false);
		summary.add(new Text("Prova - Informações Gerais"));
		
		/* Formatando os campos */
		areaConhecimento.setWidth("1030px");
	    tipoProva.setWidth("333px");
	    numAlternativas.setWidth("333px");
	    nivelProva.setWidth("333px");
	    prazo.setWidth("333px");
	    descricaoDaProva.setWidth("1030px");
	    
	    /* Deixando campos não editáveis */
		areaConhecimento.setReadOnly(true);
	    tipoProva.setReadOnly(true);
	    numAlternativas.setReadOnly(true);
	    nivelProva.setReadOnly(true);
	    prazo.setReadOnly(true);
	    descricaoDaProva.setReadOnly(true);
		
		/* Layout final de prova */
		HorizontalLayout horizontalLayout1 = new HorizontalLayout();
		horizontalLayout1.add(areaConhecimento);
		HorizontalLayout horizontalLayout2 = new HorizontalLayout();
		horizontalLayout2.add(tipoProva, numAlternativas, nivelProva);
		HorizontalLayout horizontalLayout3 = new HorizontalLayout();
		horizontalLayout3.add(descricaoDaProva);
		HorizontalLayout horizontalLayout4 = new HorizontalLayout();
		horizontalLayout4.add(prazo/*, baixarAnexo*/);
		VerticalLayout infosForm = new VerticalLayout();
		infosForm.add(horizontalLayout1, horizontalLayout2, horizontalLayout3, horizontalLayout4);
		
		/* Drop menu*/ 
		detailsProva = new Details(summary, infosForm);
		detailsProva.addThemeVariants(DetailsVariant.FILLED);
		detailsProva.setWidthFull();
		detailsProva.setMinWidth("1070px");
		detailsProva.setOpened(false);
	}
	private void dadosQuestao() {

		HorizontalLayout summary = new HorizontalLayout();
		summary.setSpacing(false);
		summary.add(new Text("Questão - Informações Gerais"));
		
		/* Formatando os campos */
		enunciadoQuestao.setWidth("700px");
		nivelDificuldadeQuestaoCombo.setWidth("310px");
		justificativaQuestao.setWidth("1025px");
		alternativaAQuestao.setWidth("700px");
		alternativaBQuestao.setWidth("700px");
		alternativaCQuestao.setWidth("700px");
		alternativaDQuestao.setWidth("700px");
		alternativaEQuestao.setWidth("700px");
		subAreasQuestao.setWidth("512px");
		
		/* Deixando campos não editaveis  */
		enunciadoQuestao.setReadOnly(true);
		nivelDificuldadeQuestaoCombo.setReadOnly(true);
		justificativaQuestao.setReadOnly(true);
		alternativaAQuestao.setReadOnly(true);
		alternativaBQuestao.setReadOnly(true);
		alternativaCQuestao.setReadOnly(true);
		alternativaDQuestao.setReadOnly(true);
		alternativaEQuestao.setReadOnly(true);
		subAreasQuestao.setReadOnly(true);
		estadoQuestao.setReadOnly(true);
		
		/* Layout final de questão */
		HorizontalLayout horizontalLayout1 = new HorizontalLayout(enunciadoQuestao,
				new VerticalLayout(nivelDificuldadeQuestaoCombo,subAreasQuestao,estadoQuestao));
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(justificativaQuestao); 
		HorizontalLayout horizontalLayout3 = new HorizontalLayout();
		VerticalLayout infosForm = new VerticalLayout(horizontalLayout1, horizontalLayout3, alternativaAQuestao, alternativaBQuestao,
								alternativaCQuestao, alternativaDQuestao, alternativaEQuestao, horizontalLayout2);
		
		/* Drop menu*/ 
		detailsQuestaoAtual = new Details(summary, infosForm);
		detailsQuestaoAtual.addThemeVariants(DetailsVariant.FILLED);
		detailsQuestaoAtual.setWidthFull();
		detailsQuestaoAtual.setMinWidth("1070px");
		detailsQuestaoAtual.setOpened(false);
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		/* Questão está null, tentei de vários jeitos, mas não foi não */
		this.questaoSelecionada = questaoService.getRepository().findById(parameter).get();
		if(questaoSelecionada==null) {
			Notification notification = new Notification("Questão não existente");
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notification.open();
			this.getUI().ifPresent(ui->ui.navigate(RevisoesView.class));
		}
		
		this.revisarQuestaoPresenter = new RevisarQuestaoPresenter(this, questaoService.getRepository());

		//operadores ternários para costumizar os metadados
		String metadadoBanca = questaoSelecionada.getState().getClass().equals(Revisao3.class)
				? "Descartar questão" : "Enviar questão à banca, para correção";
		enviarBanca.setTooltipText(metadadoBanca);

		String metadadoRevisao =
				questaoSelecionada.getState().getClass().equals(Revisao2.class)
				||
				questaoSelecionada.getState().getClass().equals(Revisao3.class)
				? "Enviar à revisão linguística" : "Enviar ao próximo revisor";
		enviarRevisao.setTooltipText(metadadoRevisao);
		
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if(optionalQuestao.get() instanceof  QuestaoObjetiva)
		questaoObjetiva = (QuestaoObjetiva) optionalQuestao.get();
		else
		this.questaoDiscussiva = (QuestaoDiscursiva)optionalQuestao.get();

		dropMenuConcurso();
		dropMenuProva(); 
		dadosQuestao();
		topicosDeRevisaoComponent();
		campoObservacao();
		
		if(!questaoSelecionada.getState().getClass().equals(null)) {
			dropMenuRevisão();
			verticalDetails.add(detailsConcurso, detailsProva);

			if(questaoSelecionada.getState().getQuestaoAnterior()!=null){
				DropDownQuestaoFactory dropDownQuestaoFactory = new DropDownQuestaoFactory(questaoSelecionada.getState()
						.getQuestaoAnterior());
				dropDownQuestaoFactory.tituloSumario.setText("Ultima versão da questão");
				//adicionar ultima versão da questão
				Details d = dropDownQuestaoFactory.getComponent();
				d.setOpened(false);
				verticalDetails.add(d);
			}
			detailsUltimaRevisao.setOpened(false);
			if(questaoObjetiva!=null)
			if(questaoObjetiva.getState().getRevisao()==null){
				detailsUltimaRevisao.setVisible(false);
			}

			detailsQuestaoAtual.setOpened(true);
			verticalDetails.add(detailsUltimaRevisao, detailsQuestaoAtual, layoutQuestao, topicosDeRevisaoLayout, orientacoesQuestao, botoesLayout);
			
			add(verticalDetails);
		}

	}


	public TextArea getSubAreasQuestao() {
		return subAreasQuestao;
	}

	public void setSubAreasQuestao(TextArea subAreasQuestao) {
		this.subAreasQuestao = subAreasQuestao;
	}

	public Button getEnviarBanca() {
		return enviarBanca;
	}

	public void setEnviarBanca(Button enviarBanca) {
		this.enviarBanca = enviarBanca;
	}

	public Button getEnviarRevisao() {
		return enviarRevisao;
	}

	public void setEnviarRevisao(Button enviarRevisao) {
		this.enviarRevisao = enviarRevisao;
	}

	public HorizontalLayout getBotoesLayout() {
		return botoesLayout;
	}

	public void setBotoesLayout(HorizontalLayout botoesLayout) {
		this.botoesLayout = botoesLayout;
	}

	public void setTopicosAnalisadosGrid(Grid<String> topicosAnalisadosGrid) {
		this.topicosAnalisadosGrid = topicosAnalisadosGrid;
	}

	public Questao getQuestaoSelecionada() {
		return questaoSelecionada;
	}

	public void setQuestaoSelecionada(Questao questaoSelecionada) {
		this.questaoSelecionada = questaoSelecionada;
	}

	public RevisarQuestaoPresenter getRevisarQuestaoPresenter() {
		return revisarQuestaoPresenter;
	}

	public void setRevisarQuestaoPresenter(RevisarQuestaoPresenter revisarQuestaoPresenter) {
		this.revisarQuestaoPresenter = revisarQuestaoPresenter;
	}

	public TextField getNomeConcurso() {
		return nomeConcurso;
	}

	public void setNomeConcurso(TextField nomeConcurso) {
		this.nomeConcurso = nomeConcurso;
	}

	public TextField getCidadeConcurso() {
		return cidadeConcurso;
	}

	public void setCidadeConcurso(TextField cidadeConcurso) {
		this.cidadeConcurso = cidadeConcurso;
	}

	public DatePicker getDataInicioConcurso() {
		return dataInicioConcurso;
	}

	public void setDataInicioConcurso(DatePicker dataInicioConcurso) {
		this.dataInicioConcurso = dataInicioConcurso;
	}

	public DatePicker getDataFimConcurso() {
		return dataFimConcurso;
	}

	public void setDataFimConcurso(DatePicker dataFimConcurso) {
		this.dataFimConcurso = dataFimConcurso;
	}

	public TextField getAreaConhecimento() {
		return areaConhecimento;
	}

	public void setAreaConhecimento(TextField areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}

	public TextField getTipoProva() {
		return tipoProva;
	}

	public void setTipoProva(TextField tipoProva) {
		this.tipoProva = tipoProva;
	}

	public TextField getNumAlternativas() {
		return numAlternativas;
	}

	public void setNumAlternativas(TextField numAlternativas) {
		this.numAlternativas = numAlternativas;
	}

	public TextField getNivelProva() {
		return nivelProva;
	}

	public void setNivelProva(TextField nivelProva) {
		this.nivelProva = nivelProva;
	}

	public DatePicker getPrazo() {
		return prazo;
	}
	

	public Prova getProva() {
		return prova;
	}

	public void setProva(Prova prova) {
		this.prova = prova;
	}

	public int getQuantAlternativas() {
		return quantAlternativas;
	}

	public void setQuantAlternativas(int quantAlternativas) {
		this.quantAlternativas = quantAlternativas;
	}

	public void setPrazo(DatePicker prazo) {
		this.prazo = prazo;
	}

	public TextArea getDescricaoDaProva() {
		return descricaoDaProva;
	}

	public void setDescricaoDaProva(TextArea descricaoDaProva) {
		this.descricaoDaProva = descricaoDaProva;
	}

	public Button getBaixarAnexo() {
		return baixarAnexo;
	}

	public void setBaixarAnexo(Button baixarAnexo) {
		this.baixarAnexo = baixarAnexo;
	}

	public TextArea getEnunciadoQuestao() {
		return enunciadoQuestao;
	}

	public void setEnunciadoQuestao(TextArea enunciadoQuestao) {
		this.enunciadoQuestao = enunciadoQuestao;
	}

	public TextArea getAlternativaAQuestao() {
		return alternativaAQuestao;
	}

	public void setAlternativaAQuestao(TextArea alternativaAQuestao) {
		this.alternativaAQuestao = alternativaAQuestao;
	}

	public TextArea getAlternativaBQuestao() {
		return alternativaBQuestao;
	}

	public void setAlternativaBQuestao(TextArea alternativaBQuestao) {
		this.alternativaBQuestao = alternativaBQuestao;
	}

	public TextArea getAlternativaCQuestao() {
		return alternativaCQuestao;
	}

	public void setAlternativaCQuestao(TextArea alternativaCQuestao) {
		this.alternativaCQuestao = alternativaCQuestao;
	}

	public TextArea getAlternativaDQuestao() {
		return alternativaDQuestao;
	}

	public void setAlternativaDQuestao(TextArea alternativaDQuestao) {
		this.alternativaDQuestao = alternativaDQuestao;
	}

	public TextArea getEstadoQuestao() {
		return estadoQuestao;
	}

	public void setEstadoQuestao(TextArea estadoQuestao) {
		this.estadoQuestao = estadoQuestao;
	}

	public TextArea getAlternativaEQuestao() {
		return alternativaEQuestao;
	}

	public void setAlternativaEQuestao(TextArea alternativaEQuestao) {
		this.alternativaEQuestao = alternativaEQuestao;
	}

	public TextArea getOrientacoesQuestao() {
		return orientacoesQuestao;
	}

	public void setOrientacoesQuestao(TextArea orientacoesQuestao) {
		this.orientacoesQuestao = orientacoesQuestao;
	}

	public VerticalLayout getLayoutgrid() {
		return layoutgrid;
	}

	public void setLayoutgrid(VerticalLayout layoutgrid) {
		this.layoutgrid = layoutgrid;
	}

	public TextField getTopicosDeRevisaoTF() {
		return topicosDeRevisaoTF;
	}

	public void setTopicosDeRevisaoTF(TextField topicosDeRevisaoTF) {
		this.topicosDeRevisaoTF = topicosDeRevisaoTF;
	}

	public VerticalLayout getTopicosDeRevisaoLayout() {
		return topicosDeRevisaoLayout;
	}

	public void setTopicosDeRevisaoLayout(VerticalLayout topicosDeRevisaoLayout) {
		this.topicosDeRevisaoLayout = topicosDeRevisaoLayout;
	}

	public HorizontalLayout getAddtopicosDeRevisaoLayout() {
		return addtopicosDeRevisaoLayout;
	}

	public void setAddtopicosDeRevisaoLayout(HorizontalLayout addtopicosDeRevisaoLayout) {
		this.addtopicosDeRevisaoLayout = addtopicosDeRevisaoLayout;
	}

	public List<String> getTopicosDeRevisao() {
		return topicosDeRevisao;
	}

	public void setTopicosDeRevisao(List<String> topicosDeRevisao) {
		this.topicosDeRevisao = topicosDeRevisao;
	}

	public TextField getNivelDificuldadeQuestaoCombo() {
		return nivelDificuldadeQuestaoCombo;
	}

	public void setNivelDificuldadeQuestaoCombo(TextField nivelDificuldadeQuestaoCombo) {
		this.nivelDificuldadeQuestaoCombo = nivelDificuldadeQuestaoCombo;
	}

	public TextArea getJustificativaQuestao() {
		return justificativaQuestao;
	}

	public void setJustificativaQuestao(TextArea justificativaQuestao) {
		this.justificativaQuestao = justificativaQuestao;
	}

	
	public ProvaService getProvaService() {
		return provaService;
	}

	public void setProvaService(ProvaService provaService) {
		this.provaService = provaService;
	}

	public QuestaoService getQuestaoService() {
		return questaoService;
	}

	public void setQuestaoService(QuestaoService questaoService) {
		this.questaoService = questaoService;
	}

	public Details getDetailsConcurso() {
		return detailsConcurso;
	}

	public void setDetailsConcurso(Details details) {
		this.detailsConcurso = details;
	}
	
	public Details getDetailsProva() {
		return detailsProva;
	}

	public void setDetailsProva(Details details) {
		this.detailsProva = details;
	}

	public VerticalLayout getVerticalDetails() {
		return verticalDetails;
	}

	public void setVerticalDetails(VerticalLayout verticalDetails) {
		this.verticalDetails = verticalDetails;
	}

	public VerticalLayout getLayoutQuestao() {
		return layoutQuestao;
	}

	public void setLayoutQuestao(VerticalLayout layoutQuestao) {
		this.layoutQuestao = layoutQuestao;
	}

	public TextField getSubareaTextField() {
		return topicosDeRevisaoTF;
	}

	public void setSubareaTextField(TextField subareaTextField) {
		this.topicosDeRevisaoTF = subareaTextField;
	}

	public Button getAdicionarButton() {
		return adicionarButton;
	}

	public void setAdicionarButton(Button adicionarButton) {
		this.adicionarButton = adicionarButton;
	}

	public Button getAddButton() {
		return addButton;
	}

	public void setAddButton(Button addButton) {
		this.addButton = addButton;
	}

	public VerticalLayout getSubAreaLayout() {
		return topicosDeRevisaoLayout;
	}

	public void setSubAreaLayout(VerticalLayout subAreaLayout) {
		this.topicosDeRevisaoLayout = subAreaLayout;
	}

	public HorizontalLayout getAddSubAreaLayout() {
		return addtopicosDeRevisaoLayout;
	}

	public void setAddSubAreaLayout(HorizontalLayout addSubAreaLayout) {
		this.addtopicosDeRevisaoLayout = addSubAreaLayout;
	}

	public List<String> getSubAreas() {
		return topicosDeRevisao;
	}

	public void setSubAreas(List<String> subAreas) {
		this.topicosDeRevisao = subAreas;
	}

	public Grid<String> getTopicosAnalisadosGrid() {
		return topicosAnalisadosGrid;
	}
	
	public Details getDetailsQuestaoAtual() {
		return detailsQuestaoAtual;
	}

	public void setDetailsQuestaoAtual(Details detailsQuestaoAtual) {
		this.detailsQuestaoAtual = detailsQuestaoAtual;
	}

	public HashMap<String, Atendimento> getTopicosAnalisadosHashMap() {
		return topicosAnalisadosHashMap;
	}

	public void setTopicosAnalisadosHashMap(HashMap<String, Atendimento> topicosAnalisadosHashMap) {
		this.topicosAnalisadosHashMap = topicosAnalisadosHashMap;
	}
}
