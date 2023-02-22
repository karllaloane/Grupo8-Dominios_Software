package br.ufg.sep.views.revisao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.security.PermitAll;

import br.ufg.sep.views.revisao.presenter.RevisaoLinguagemQuestaoPresenter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Atendimento;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.correcao.presenter.CorrecaoObjetivaBancaPresenter;
import br.ufg.sep.views.revisao.presenter.RevisarQuestaoPresenter;

@Route(value="revisar-linguagem-questao", layout = MainLayout.class)
@PageTitle("Revisar Linguagem")
@PermitAll

public class RevisaoLinguagemQuestaoView extends VerticalLayout  implements HasUrlParameter<Long>  {

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

	private TextArea estadoQuestao = new TextArea("Estado da questão");

	/* Imputs gerais */
	private Button enviarBanca = new Button("Enviar questão para banca", new Icon(VaadinIcon.CHECK_CIRCLE));

	private ProvaService provaService;
	private Prova prova;
	private int quantAlternativas;
	private QuestaoService questaoService;
	VerticalLayout verticalDetails = new VerticalLayout();
	VerticalLayout layoutQuestao = new VerticalLayout();
	VerticalLayout layoutgrid = new VerticalLayout();
	HorizontalLayout botoesLayout = new HorizontalLayout(enviarBanca);
	private Details details1;
	private Details details2;
	private Details details3;
	private TextField topicosDeRevisaoTF;
	private Button adicionarButton;
    private Grid<String> topicosAnalisadosGrid;
	private Button addButton;
	private VerticalLayout topicosDeRevisaoLayout;
	private HorizontalLayout addtopicosDeRevisaoLayout;
	private List<String> topicosDeRevisao;
	private HashMap<String, Atendimento> topicosAnalisadosHashMap = new HashMap<>();
	private Questao questaoSelecionada;
	private RevisarQuestaoPresenter revisarQuestaoPresenter;

	public RevisaoLinguagemQuestaoView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		topicosAnalisadosGrid = new Grid<>();
		
		dropMenuConcurso(); 
		dropMenuProva(); 
		dadosQuestao();
		campoObservacao();
		
		verticalDetails.add(details1, details2, details3, enviarBanca);
		
		add(verticalDetails);

		add();
	}
	
	public void campoObservacao() {
		orientacoesQuestao.setWidth("1030px");
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
		subAreasQuestao.setWidth("512px");
		
		/* Deixando campos não editaveis  */
		nivelDificuldadeQuestaoCombo.setReadOnly(true);
		justificativaQuestao.setReadOnly(true);
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
		details3 = new Details(summary, infosForm);
		details3.addThemeVariants(DetailsVariant.FILLED);
		details3.setWidthFull();
		details3.setMinWidth("1070px");
		details3.setOpened(true);
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

	public Grid<String> getTopicosAnalisadosGrid() {
		return topicosAnalisadosGrid;
	}
	
	public Details getDetails3() {
		return details3;
	}

	public void setDetails3(Details details3) {
		this.details3 = details3;
	}

	public HashMap<String, Atendimento> getTopicosAnalisadosHashMap() {
		return topicosAnalisadosHashMap;
	}

	public void setTopicosAnalisadosHashMap(HashMap<String, Atendimento> topicosAnalisadosHashMap) {
		this.topicosAnalisadosHashMap = topicosAnalisadosHashMap;
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {

		RevisaoLinguagemQuestaoPresenter presenter = new RevisaoLinguagemQuestaoPresenter(
				questaoService.getRepository().findById(parameter).get(),
				this,
				questaoService.getRepository()
		);


	}
}
