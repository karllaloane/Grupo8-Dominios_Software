package br.ufg.sep.views.revisao;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.presenter.NovaQuestaoObjetivaPresenter;

@Route(value="gerenciar-revisao", layout = MainLayout.class)
@PageTitle("Revisão")
@PermitAll

public class GerenciarRevisaoView extends HorizontalLayout /* implements HasUrlParameter<Long> */{
	
	/* Imputs do concurso */ 
	private TextField nomeConcurso = new TextField("Nome", "", "");
	private TextField cidadeConcurso = new TextField("Cidade", "", "");
	private DatePicker dataInicioConcurso = new DatePicker("Data de início");
	private DatePicker dataFimConcurso = new DatePicker("Data do fim");
	
	/* Imputs da prova */
	private TextField areaConhecimento = new TextField("Área de conhecimento", "", "");
    private TextField tipoProva = new TextField("Tipo de Prova", "", "");
    private TextField numAlternativas = new TextField("Número de Alternativas ", "", "");
    private TextField nivelProva = new TextField("Nível da Prova", "", "");
    private DatePicker prazo = new DatePicker("Prazo");
    private TextArea descricaoDaProva = new TextArea("Descriçãop da Prova", "", "");
    private Button baixarAnexo = new Button("Baixar Anexo"); 
    
    /* Imputs da questão */
    private TextArea subAreasQuestao = new TextArea("Sub-áreas da questão ", "", "");
    private TextArea enunciadoQuestao = new TextArea("Enunciado", "", "");
	private TextField nivelDificuldadeQuestaoCombo = new TextField("Nível de Dificuldade", "", "");
	private TextArea justificativaQuestao = new TextArea("Justificativa da Alternativa Correta", "", "");
	private TextArea alternativaAQuestao = new TextArea("A) ", "", "");
	private TextArea alternativaBQuestao = new TextArea("B) ", "", "");
	private TextArea alternativaCQuestao = new TextArea("C) ", "", "");
	private TextArea alternativaDQuestao = new TextArea("D) ", "", "");
	private TextArea alternativaEQuestao = new TextArea("E) ", "", "");
	private TextArea observacaouestao = new TextArea("Observação da Revisão", "", "");
	
	/* Imputs gerais */
	private Button enviarBanca = new Button("Voltar para a Banca de Elaboração");
	private Button enviarRevisao = new Button("Enviar para a próxima Etapa");
	private ProvaService provaService;
	private Prova prova;
	private int quantAlternativas;
	private QuestaoService questaoService;
	VerticalLayout verticalDetails = new VerticalLayout();
	VerticalLayout layoutQuestao = new VerticalLayout();
	VerticalLayout layoutgrid = new VerticalLayout();
	HorizontalLayout botoesLayout = new HorizontalLayout(enviarBanca, enviarRevisao);
	private Details details1;
	private Details details2;
	private Details details3;
	private TextField topicosDeRevisaoTF;
	private Button adicionarButton;
    private Grid<String> grid;
	private Button addButton;
	private VerticalLayout topicosDeRevisaoLayout;
	private HorizontalLayout addtopicosDeRevisaoLayout;
	private List<String> topicosDeRevisao;

	public GerenciarRevisaoView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		grid = new Grid<>();

		dropMenuConcurso(); 
		dropMenuProva(); 
		dadosQuestao();
		topicosDeRevisaoComponent();
		campoObservacao(); 
		
		verticalDetails.add(details1, details2, details3, layoutQuestao, topicosDeRevisaoLayout, observacaouestao, botoesLayout);
		
		add(verticalDetails);
	}
	
	
	public void campoObservacao() {
		observacaouestao.setWidth("1030px");
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
		
		grid.setWidth("1030px");
		
		topicosDeRevisaoLayout.add(new Span("Tópicos avaliados na Revisão"), grid, addtopicosDeRevisaoLayout);
		
		grid.setItems(topicosDeRevisao);
		grid.setAllRowsVisible(true);
		
		//adicionando a coluna
		//conterá o arraylist e o botão de remover
		grid.addColumn(item -> item).setFlexGrow(1);
		
		grid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkAtende, item) -> {
                	checkAtende.setLabel("Atende");
                })).setWidth("130px").setFlexGrow(0).setKey("atende");
		
		grid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkAtendeParcialmente, item) -> {
                	checkAtendeParcialmente.setLabel("Atende Parcialmente");
                	/* Ajeitar o taamanho disso aqui */
                })).setWidth("220px").setFlexGrow(0).setKey("atende-parcialmente");
		
		grid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkNaoAtende, item) -> {
                	checkNaoAtende.setLabel("Não Atende");
                })).setWidth("130px").setFlexGrow(0).setKey("nao-atende");
		
		grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, item) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_TERTIARY);
                    button.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE_O));
                    button.addClickListener(e -> this.removeSubArea(item));
                })).setWidth("130px").setFlexGrow(0).setKey("remover");
		
		// listener pra adicionar as subareas no grid
		addButton.addClickListener(e ->{
			if(!this.topicosDeRevisaoTF.isEmpty()) {
				this.topicosDeRevisao.add(topicosDeRevisaoTF.getValue());
			}
			topicosDeRevisaoTF.clear();
			atualizaGrid();
		});
		
		if(topicosDeRevisao.size() == 0) {
			grid.setVisible(false);
		} else {
			grid.setVisible(true);
		}
		
		this.setPadding(false);
	}
	
	//atualizar o grid
	public void atualizaGrid() {
		if(topicosDeRevisao.size() == 0) {
			grid.setVisible(false);
		} else {
			grid.setItems(this.topicosDeRevisao);
			grid.getDataProvider().refreshAll();
			grid.setVisible(true);
		}
	}

	//metodo pra remover uma subarea da lista
	private void removeSubArea(String item) {
		topicosDeRevisao.remove(item);
		atualizaGrid();
	}
	
	//pra poder setar a edicao falta quando 
	//for apenas visualização
	public void setEdicaoFalse() {
		this.addButton.setVisible(false);
		this.topicosDeRevisaoTF.setVisible(false);
		this.addtopicosDeRevisaoLayout.setVisible(false);
		grid.getColumnByKey("remover").setVisible(false);;
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
		nomeConcurso.setEnabled(false);
		cidadeConcurso.setEnabled(false);
		dataInicioConcurso.setEnabled(false);
		dataFimConcurso.setEnabled(false);
		
		/* Layout final do concurso */
		HorizontalLayout horizontalLayout1 = new HorizontalLayout();
		horizontalLayout1.add(nomeConcurso, dataInicioConcurso);
		HorizontalLayout horizontalLayout2 = new HorizontalLayout();
		horizontalLayout2.add(cidadeConcurso, dataFimConcurso);
		VerticalLayout infosForm = new VerticalLayout();
		infosForm.add(horizontalLayout1, horizontalLayout2);
		
		/* Drop menu*/ 
		details1 = new Details(summary, infosForm);
		details1.addThemeVariants(DetailsVariant.FILLED);
		details1.setWidthFull();
		details1.setMinWidth("1070px");
		details1.setOpened(false);
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
		areaConhecimento.setEnabled(false);
	    tipoProva.setEnabled(false);
	    numAlternativas.setEnabled(false);
	    nivelProva.setEnabled(false);
	    prazo.setEnabled(false);
	    descricaoDaProva.setEnabled(false);
		
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
		details2 = new Details(summary, infosForm);
		details2.addThemeVariants(DetailsVariant.FILLED);
		details2.setWidthFull();
		details2.setMinWidth("1070px");
		details2.setOpened(false);
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
		subAreasQuestao.setWidth("1025px");
		
		/* Deixando campos não editaveis  */
		enunciadoQuestao.setEnabled(false);
		nivelDificuldadeQuestaoCombo.setEnabled(false);
		justificativaQuestao.setEnabled(false);
		alternativaAQuestao.setEnabled(false);
		alternativaBQuestao.setEnabled(false);
		alternativaCQuestao.setEnabled(false);
		alternativaDQuestao.setEnabled(false);
		alternativaEQuestao.setEnabled(false);
		subAreasQuestao.setEnabled(false);
		
		/* Layout final de questão */
		HorizontalLayout horizontalLayout1 = new HorizontalLayout(enunciadoQuestao, nivelDificuldadeQuestaoCombo);
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(justificativaQuestao); 
		HorizontalLayout horizontalLayout3 = new HorizontalLayout(subAreasQuestao); 
		VerticalLayout infosForm = new VerticalLayout(horizontalLayout1, horizontalLayout3, alternativaAQuestao, alternativaBQuestao, 
								alternativaCQuestao, alternativaDQuestao, alternativaEQuestao, horizontalLayout2);
		
		/* Drop menu*/ 
		details3 = new Details(summary, infosForm);
		details3.addThemeVariants(DetailsVariant.FILLED);
		details3.setWidthFull();
		details3.setMinWidth("1070px");
		details3.setOpened(false);
	}
	
	/*
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalProva = provaService.getRepository().findById(parameter);
		if (optionalProva.isPresent()) {
			prova = optionalProva.get();
			//this.provaId = prova.getId();
			
			//tornando a quinta alternativa falta caso a prova seja objetiva com 4 alternativas
			if(prova.getTipo() == TipoProva.OBJETIVA_4) {
				quantAlternativas = 4;	
				alternativaEQuestao.setVisible(false);
			} else 
				quantAlternativas = 5;

			  this.presenter = new NovaQuestaoObjetivaPresenter(provaService, questaoService, this); //iniciar o presenter
		} 
		
	} */ 
    
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

	public TextArea getAlternativaEQuestao() {
		return alternativaEQuestao;
	}

	public void setAlternativaEQuestao(TextArea alternativaEQuestao) {
		this.alternativaEQuestao = alternativaEQuestao;
	}

	public TextArea getObservacaouestao() {
		return observacaouestao;
	}

	public void setObservacaouestao(TextArea observacaouestao) {
		this.observacaouestao = observacaouestao;
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

	public Details getDetails1() {
		return details1;
	}

	public void setDetails1(Details details) {
		this.details1 = details;
	}
	
	public Details getDetails2() {
		return details2;
	}

	public void setDetails2(Details details) {
		this.details2 = details;
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

	public Grid<String> getGrid() {
		return grid;
	}
	
	public Details getDetails3() {
		return details3;
	}

	public void setDetails3(Details details3) {
		this.details3 = details3;
	}
}
