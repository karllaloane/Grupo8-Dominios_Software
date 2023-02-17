package br.ufg.sep.views.gerenciarProvas;

import javax.annotation.security.RolesAllowed;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.gerenciarProvas.presenter.EditarProvaPresenter;
import br.ufg.sep.views.gerenciarProvas.presenter.NovaProvaPresenter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.views.MainLayout;

import java.io.InputStream;
import java.util.Optional;


@Route(value="visualizar-prova", layout=MainLayout.class)
@PageTitle("Visualizar Provas")
@RolesAllowed({"ADMIN","PED"})

public class VisualizarProvaView extends VerticalLayout implements HasUrlParameter<Long> {
	
	Button cancel = new Button("Voltar");
	Button arquivo = new Button("Baixar Arquivo anexado");
	Prova prova; 
	private Long concursoId; // só é instanciado após o construtor. (Só deve ser usado em Listeners)
    ProvaService provaService;
    private ConcursoService concursoService;
    private CadastroRepository cadastroRepository;
    private Concurso concurso; // só é instanciado após o construtor. (Só deve ser usado em Listeners)
    
    /*Inputs para cadastrar uma nova Prova*/
    private TextField nomeConcurso = new TextField();
    private TextField areaConhecimento = new TextField();
    private TextField numQuestoes = new TextField();
    private TextField tipoProva = new TextField();
    private TextField nivelNumAlternativas = new TextField();
    private TextField nivelProva = new TextField();
    private TextField revisorTecnico1 = new TextField();
    private TextField revisorTecnico2 = new TextField();
    private TextField revisorTecnico3 = new TextField();
    private TextField revisorLinguagem = new TextField();
    private TextField membroBanca = new TextField();
    private DatePicker prazo;
    private TextArea descricaoDaProva = new TextArea();
    HorizontalLayout layoutFinal = new HorizontalLayout();
    private NovaProvaPresenter presenter;
    
	
    public VisualizarProvaView(ProvaService PS) {
		
		this.provaService = PS;
		
		criarTela();
		
		// EditarProvaPresenter formPresenter = new EditarProvaPresenter(this, PS);
        
        setPadding(true);
        
	}	
	
	private void criarTela() {
		/* Formatando o atributo prazo do tipo DatePicker para dd/MM/yyyy*/ 
    	DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
		singleFormatI18n.setDateFormat("dd/MM/yyyy");
		
		/*Inicializando o atributo e formatando*/
		prazo = new DatePicker("Prazo de Entrega");
		prazo.setI18n(singleFormatI18n);
		prazo.setPlaceholder("DD/MM/AAAA");
		prazo.setWidth("215px");
		prazo.setEnabled(false);
		
		/* Campo Nome do Concurso*/
        nomeConcurso.setLabel("Concurso pertencente");
        nomeConcurso.setWidth("444px");
        nomeConcurso.setEnabled(false);
        
        /*Alterando tema botão*/
        arquivo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        /* Campo Area de conhecimento*/
        areaConhecimento.setLabel("Area de conhecimento");
        areaConhecimento.setWidth("444px");
        areaConhecimento.setEnabled(false);
        
        /* Campo Numero de questões*/
        numQuestoes.setLabel("Numero de questoes");
        numQuestoes.setWidth("215px");
        numQuestoes.setEnabled(false);
        
        /*Campo descrição da prova*/
        descricaoDaProva.setLabel("Descrição da Prova");
        descricaoDaProva.setWidth("936px");
        descricaoDaProva.setHeight("148px");
        descricaoDaProva.setEnabled(false);
        
        /*Revidores e membro da banca*/
        membroBanca.setLabel("Membro da banca");
        membroBanca.setWidth("444px");
        membroBanca.setEnabled(false);
        revisorTecnico1.setLabel("Revisor Técnico 1");
        revisorTecnico1.setWidth("444px");
        revisorTecnico1.setEnabled(false);
        revisorTecnico2.setLabel("Revisor Técnico 2");
        revisorTecnico2.setWidth("444px");
        revisorTecnico2.setEnabled(false);
        revisorTecnico3.setLabel("Revisor Técnico 3");
        revisorTecnico3.setWidth("444px");
        revisorTecnico3.setEnabled(false);
        revisorLinguagem.setLabel("Revisor de Linguagem");
        revisorLinguagem.setWidth("444px");
        revisorLinguagem.setEnabled(false);
        
        /*Campo de escolher tipo de prova*/
        tipoProva.setLabel("Tipo de prova:");
        tipoProva.setWidth("444px");
        tipoProva.setEnabled(false);
        
        /*RadioBox-down se selecionar prova objetiva*/ 
        nivelNumAlternativas.setLabel("Quantidade de alternativas: ");
        nivelNumAlternativas.setWidth("444px");
        nivelNumAlternativas.setVisible(false);
        nivelNumAlternativas.setEnabled(false);
    
        
        /*Campo de escolher nível de prova*/
        nivelProva.setLabel("Nível da prova:");
        nivelProva.setWidth("444px");
        nivelProva.setEnabled(false);

        /* Disposição de todos os elementos*/
        HorizontalLayout contatinterCima = new HorizontalLayout(numQuestoes, prazo);
        VerticalLayout verticalDescricao= new VerticalLayout(descricaoDaProva);
        HorizontalLayout horizontalbotoes= new HorizontalLayout(cancel, arquivo);
        VerticalLayout aux = new VerticalLayout(horizontalbotoes);
        VerticalLayout verticalLayoutdireito = new VerticalLayout(membroBanca, revisorTecnico1, revisorTecnico2, revisorTecnico3, revisorLinguagem);
        VerticalLayout verticalLayoutEsquerdo = new VerticalLayout(nomeConcurso, contatinterCima, areaConhecimento, tipoProva, nivelNumAlternativas, nivelProva);
        HorizontalLayout l = new HorizontalLayout(verticalLayoutEsquerdo, verticalLayoutdireito);
        VerticalLayout layoutFinal = new VerticalLayout(l, verticalDescricao, aux);
        
        add(layoutFinal);
	}
    
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		Optional<Prova> optionalProva = provaService.getRepository().findById(parameter);
		
		if (optionalProva.isPresent()) {
			prova = optionalProva.get();
			
			this.numQuestoes.setValue(Integer.toString(prova.getNumeroQuestoes()));
			this.prazo.setValue(prova.getDataEntrega());
			this.areaConhecimento.setValue(prova.getAreaConhecimento());
			this.descricaoDaProva.setValue(prova.getDescricao());
			
			
			
		} else {
			Notification notification = Notification
			        .show("Impossível acessar a prova");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}
	
     /* Gets e Sets de todos os campos */ 
    public Long getConcursoId() {
		return concursoId;
	}


	public void setConcursoId(Long concursoId) {
		this.concursoId = concursoId;
	}


	public ProvaService getProvaService() {
		return provaService;
	}


	public void setProvaService(ProvaService provaService) {
		this.provaService = provaService;
	}


	public ConcursoService getConcursoService() {
		return concursoService;
	}


	public void setConcursoService(ConcursoService concursoService) {
		this.concursoService = concursoService;
	}


	public CadastroRepository getCadastroRepository() {
		return cadastroRepository;
	}


	public void setCadastroRepository(CadastroRepository cadastroRepository) {
		this.cadastroRepository = cadastroRepository;
	}


	public Concurso getConcurso() {
		return concurso;
	}


	public void setConcurso(Concurso concurso) {
		this.concurso = concurso;
	}


	public TextField getNomeConcurso() {
		return nomeConcurso;
	}


	public void setNomeConcurso(TextField nomeConcurso) {
		this.nomeConcurso = nomeConcurso;
	}


	public TextField getAreaConhecimento() {
		return areaConhecimento;
	}


	public void setAreaConhecimento(TextField areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}


	public TextField getNumQuestoes() {
		return numQuestoes;
	}


	public void setNumQuestoes(TextField numQuestoes) {
		this.numQuestoes = numQuestoes;
	}


	public TextArea getDescricaoDaProva() {
		return descricaoDaProva;
	}


	public void setDescricaoDaProva(TextArea descricaoDaProva) {
		this.descricaoDaProva = descricaoDaProva;
	}


	public DatePicker getPrazo() {
		return prazo;
	}


	public void setPrazo(DatePicker prazo) {
		this.prazo = prazo;
	}


	public NovaProvaPresenter getPresenter() {
		return presenter;
	}


	public void setPresenter(NovaProvaPresenter presenter) {
		this.presenter = presenter;
	}

}
