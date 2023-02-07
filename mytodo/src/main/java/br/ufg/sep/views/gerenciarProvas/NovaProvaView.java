package br.ufg.sep.views.gerenciarProvas;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerenciarProvas.presenter.NovaProvaPresenter;
import br.ufg.sep.views.permissoes.GridCadastroFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

import java.io.InputStream;
import java.util.Optional;

@Route(value = "nova-prova", layout = MainLayout.class)
@PageTitle("Nova Prova")
@RolesAllowed({"ADMIN","PROF"})
public class NovaProvaView extends VerticalLayout implements HasUrlParameter<Long> {
	
    private Concurso concurso; // só é instanciado após o construtor. (Só deve ser usado em Listeners)
    private ProvaService provaService;
    private ConcursoService concursoService;
    
    /*Inputs para cadastrar uma nova Prova*/
    private TextField nomeConcurso = new TextField();
    private TextField areaConhecimento = new TextField();
    private TextField numQuestoes = new TextField();
    private TextField colaboradorAssociado = new TextField();
    private TextArea descricaoDaProva = new TextArea();
    private DatePicker prazo;
    
    /* MultiFileMemoryBuffer e Upload para baixar arquivos*/
    private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload upload = new Upload(buffer);

    private NovaProvaPresenter presenter;
    private Grid<Cadastro> colaboradoresGrid;
    private Button salvarButton = new Button("Salvar"); // Btn: Button


    public NovaProvaView(ProvaService provaService, ConcursoService concursoService,
                         CadastroRepository cadastroRepository){
    	
    	this.provaService = provaService;
        this.concursoService = concursoService;
        this.setAlignItems(Alignment.CENTER); // Alinhar a NovaProvaView no geral
    	
    	/* Formatando o atributo prazo do tipo DatePicker para dd/MM/yyyy*/ 
    	DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
		singleFormatI18n.setDateFormat("dd/MM/yyyy");
		
		/*Inicializando o atributo e formatando*/
		prazo = new DatePicker("Prazo de Entrega");
		prazo.setI18n(singleFormatI18n);
		prazo.setPlaceholder("DD/MM/AAAA");
		
		/* Campo Nome do Concurso*/
        nomeConcurso.setLabel("Concurso pertencente");
        nomeConcurso.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); // Alinhar o texto dentro do nomeConcurso
        nomeConcurso.setReadOnly(true);
        nomeConcurso.setWidth("610px");

        /* Campo Area de conhecimento e Numero de questões*/
        areaConhecimento.setLabel("Area de conhecimento");
        numQuestoes.setLabel("Numero de questoes");
        
        /*Campo descrição da prova*/
        descricaoDaProva.setLabel("Descrição da Prova");
        descricaoDaProva.setWidth("610px");
        
        /*Upload de arquivos*/
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
        });
        upload.setWidth("610px");
        
        /* Campo colaborador associado */
        colaboradorAssociado.setLabel("Colaborador associado");
        colaboradorAssociado.setReadOnly(true);
        colaboradorAssociado.setWidth("610px");
        
        /* Disposição horizontal dos elementos areaConhecimento,numQuestoes, prazo*/
        HorizontalLayout contatinterCima = new HorizontalLayout(areaConhecimento,numQuestoes, prazo);

        /* Lista de colaboradores*/
        this.colaboradoresGrid = new GridCadastroFactory(cadastroRepository).getGrid();
        this.colaboradoresGrid.setHeight("300px");

        add(nomeConcurso, contatinterCima, descricaoDaProva, colaboradorAssociado, colaboradoresGrid, upload, salvarButton);
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) { //Método executado após o construtor.

        concurso = concursoService.getRepository().findById(parameter).get();
        nomeConcurso.setValue(concurso.getNome());
        this.presenter = new NovaProvaPresenter(this, provaService);


    }
    /****************************************************************/
    //Geters and Setters

    public Concurso getConcurso() {
        return concurso;
    }

    public void setConcurso(Concurso concurso) {
        this.concurso = concurso;
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

    public TextField getColaboradorAssociado() {
        return colaboradorAssociado;
    }

    public void setColaboradorAssociado(TextField colaboradorAssociado) {
        this.colaboradorAssociado = colaboradorAssociado;
    }

    public NovaProvaPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(NovaProvaPresenter presenter) {
        this.presenter = presenter;
    }

    public Grid<Cadastro> getColaboradoresGrid() {
        return colaboradoresGrid;
    }

    public void setColaboradoresGrid(Grid<Cadastro> colaboradoresGrid) {
        this.colaboradoresGrid = colaboradoresGrid;
    }

	public DatePicker getPrazo() {
		return prazo;
	}

	public void setPrazo(DatePicker prazo) {
		this.prazo = prazo;
	}
	
    public Button getSalvarButton() {
        return salvarButton;
    }

    public void setSalvarButton(Button salvarButton) {
        this.salvarButton = salvarButton;
    }

	public TextArea getDescricaoDaProva() {
		return descricaoDaProva;
	}

	public void setDescricaoDaProva(TextArea descricaoDaProva) {
		this.descricaoDaProva = descricaoDaProva;
	}



}
