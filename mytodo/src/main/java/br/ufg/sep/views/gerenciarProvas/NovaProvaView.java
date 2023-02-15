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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBox.ItemFilter;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Route(value = "nova-prova", layout = MainLayout.class)
@PageTitle("Nova Prova")
@RolesAllowed({"ADMIN","PROF"})
public class NovaProvaView extends VerticalLayout implements HasUrlParameter<Long> {

    private Long concursoId; // só é instanciado após o construtor. (Só deve ser usado em Listeners)
    private ProvaService provaService;
    private ConcursoService concursoService;

    private CadastroRepository cadastroRepository;

    private Concurso concurso; // só é instanciado após o construtor. (Só deve ser usado em Listeners)
    
    /*Inputs para cadastrar uma nova Prova*/
    private TextField nomeConcurso = new TextField();
    private TextField areaConhecimento = new TextField();
    private TextField numQuestoes = new TextField();
    private TextArea descricaoDaProva = new TextArea();
    private DatePicker prazo;
    private RadioButtonGroup<String> radioTipoProva = new RadioButtonGroup<>();
    private RadioButtonGroup<String> radioNivelProva = new RadioButtonGroup<>();
    private RadioButtonGroup<String> radioNivelNumAlternativas = new RadioButtonGroup<>();
    private Button salvarButton = new Button("Salvar"); // Btn: Button
    private NovaProvaPresenter presenter;

    /* MultiFileMemoryBuffer e Upload para baixar arquivos*/
    private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload upload = new Upload(buffer);

    private ComboBox<Cadastro> comboBoxMembroRevisorLinguagem;
    private ComboBox<Cadastro> comboBoxMembroBancaQuestao;
    private ComboBox<Cadastro> comboBoxMembroRevisorTecnico1;
    private ComboBox<Cadastro> comboBoxMembroRevisorTecnico2;

    private ComboBox<Cadastro> comboBoxMembroRevisorTecnico3;


    public NovaProvaView(ProvaService provaService, ConcursoService concursoService,
                         CadastroRepository cadastroRepository){
        this.provaService = provaService;
        this.concursoService = concursoService;
        this.cadastroRepository = cadastroRepository;
    	this.setAlignItems(Alignment.CENTER);
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
        descricaoDaProva.setHeight("178px");
           
        /*Label do upload de arquivos*/
        Label dropDisabledLabel = new Label("Adicionar um anexo");
        dropDisabledLabel.getStyle().set("font-weight", "100");
        
        /*Upload de arquivos*/
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
        });
        upload.setWidth("300px");
        
        /*Campo de escolher tipo de prova*/
        radioTipoProva.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioTipoProva.setLabel("Escolha o tipo de prova:");
        radioTipoProva.setItems("Objetiva", "Discursiva", "Redação");
        
        /*RadioBox-down se selecionar prova objetiva*/ 
        // radioNivelNumAlternativas.addThemeVariants(RadioGroupVariant.);
        radioNivelNumAlternativas.setLabel("Quantidade de alternativas: ");
        radioNivelNumAlternativas.setItems("4", "5");
        radioNivelNumAlternativas.setVisible(false); /*Fica invisivel, no NovaProvaPresenter deve aparecer quando a opção
         											   "objetiva" estiver selecionada*/
        
        
        /*Campo de escolher nível de prova*/
        radioNivelProva.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioNivelProva.setLabel("Escolha o nível da prova:");
        radioNivelProva.setItems("Fundamental", "Médio", "Superior");



        
        /* Disposição de todos os elementos*/
        HorizontalLayout contatinterCima = new HorizontalLayout(areaConhecimento,numQuestoes, prazo);
        VerticalLayout verticalLayoutdireito = new VerticalLayout(radioTipoProva, radioNivelNumAlternativas,radioNivelProva, dropDisabledLabel, upload);
        VerticalLayout verticalLayoutEsquerdo = new VerticalLayout(nomeConcurso, contatinterCima, 
        		descricaoDaProva);
        HorizontalLayout layoutFinal = new HorizontalLayout(verticalLayoutEsquerdo, verticalLayoutdireito);
        
        /*Metodo que mostra layoutFinal na tela*/
        add(layoutFinal); 
        
        /*Metodo que apresenta o ComboBox na tela*/
        ComboBoxPresentation();
        
        /*Metodo que mostra elaboradoresGrid, salvarButton na tela*/
        add(salvarButton);
        

    }




    //Para facilitar a configuração de cada comboBox no presenter
    // -->>>configComboBox()<<-- no presenter
    List<ComboBox<Cadastro>> utilArrayComboBoxCadastro;
    public void ComboBoxPresentation() {
    	
    	/* Filtro do ComboBox*/
         ItemFilter<Cadastro> filter = (cadastro,
                filterString) -> (cadastro.getNome() + " "
                        + cadastro.getCpf())
                        .toLowerCase().indexOf(filterString.toLowerCase()) > -1; 

    	/*Criação do comboBoxMembroBancaQuestao*/

        comboBoxMembroBancaQuestao = new ComboBox<>("Membro da banca de questão:");
        comboBoxMembroBancaQuestao.setRenderer(createRenderer());/* Função abaixo*/
        comboBoxMembroBancaQuestao.setItemLabelGenerator(cad->
                cad.getNome() == null ? "" : cad.getNome()
        );
        comboBoxMembroBancaQuestao.getStyle().set("--vaadin-combo-box-overlay-width", "16em");
        comboBoxMembroBancaQuestao.setWidth("610px");
        
        comboBoxMembroRevisorTecnico1 = new ComboBox<>("Revisor técnico 1:");
        comboBoxMembroRevisorTecnico1.setRenderer(createRenderer());
        comboBoxMembroRevisorTecnico1.setItemLabelGenerator(cad->
                cad.getNome() == null ? "" : cad.getNome()
        );
        comboBoxMembroRevisorTecnico1.getStyle().set("--vaadin-combo-box-overlay-width", "16em");
        comboBoxMembroRevisorTecnico1.setWidth("610px");
        
        
        comboBoxMembroRevisorTecnico2 = new ComboBox<>("Revisor Técnico 2:");
        comboBoxMembroRevisorTecnico2.setRenderer(createRenderer()); /* Função abaixo*/
        comboBoxMembroRevisorTecnico2.setItemLabelGenerator(cad->
                cad.getNome() == null ? "" : cad.getNome()
        );
        comboBoxMembroRevisorTecnico2.getStyle().set("--vaadin-combo-box-overlay-width", "16em");
        comboBoxMembroRevisorTecnico2.setWidth("610px");

        comboBoxMembroRevisorTecnico3 = new ComboBox<>("Revisor Técnico 3:");
        comboBoxMembroRevisorTecnico3.setRenderer(createRenderer()); /* Função abaixo*/
        comboBoxMembroRevisorTecnico3.setItemLabelGenerator(cad->
                cad.getNome() == null ? "" : cad.getNome()
        );
        comboBoxMembroRevisorTecnico3.getStyle().set("--vaadin-combo-box-overlay-width", "16em");
        comboBoxMembroRevisorTecnico3.setWidth("610px");
        
        
        comboBoxMembroRevisorLinguagem = new ComboBox<>("Revisor de Linguagem:");
        comboBoxMembroRevisorLinguagem.setRenderer(createRenderer()); /* Função abaixo*/
        comboBoxMembroRevisorLinguagem.setItemLabelGenerator(cad->
                cad.getNome() == null ? "" : cad.getNome()
        );
        comboBoxMembroRevisorLinguagem.getStyle().set("--vaadin-combo-box-overlay-width", "16em");
        comboBoxMembroRevisorLinguagem.setWidth("610px");
        
        VerticalLayout verticalLayout = new VerticalLayout(
                comboBoxMembroBancaQuestao,
                comboBoxMembroRevisorTecnico1,
                comboBoxMembroRevisorTecnico2,
                comboBoxMembroRevisorLinguagem
        );

        utilArrayComboBoxCadastro =List.of(
                comboBoxMembroBancaQuestao,
                comboBoxMembroRevisorTecnico1,
                comboBoxMembroRevisorTecnico2,
                comboBoxMembroRevisorTecnico3,
                comboBoxMembroRevisorLinguagem);

        verticalLayout.setAlignItems(Alignment.CENTER);
        add(verticalLayout);
    }
    
    private Renderer<Cadastro> createRenderer() {
    	
    	/*Formatando para o CPF ficar abaixo do Nome no ComboBox*/
    	StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        
        tpl.append("  <div>");
        tpl.append("    ${item.nome}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.cpf}</div>");
        tpl.append("  </div>"); 

        return LitRenderer.<Cadastro> of(tpl.toString())
                .withProperty("nome", Cadastro::getNome)
                .withProperty("cpf", Cadastro::getCpf);
    }
    
    
    

    @Override
    public void setParameter(BeforeEvent event, Long parameter) { //Método executado após o construtor.
        concursoId = parameter;
        concurso = concursoService.getRepository().findById(parameter).get();
        nomeConcurso.setValue(concurso.getNome());
        this.presenter = new NovaProvaPresenter(this, provaService);
    }

    public ComboBox<Cadastro> getComboBoxMembroRevisorTecnico3() {
        return comboBoxMembroRevisorTecnico3;
    }

    public void setComboBoxMembroRevisorTecnico3(ComboBox<Cadastro> comboBoxMembroRevisorTecnico3) {
        this.comboBoxMembroRevisorTecnico3 = comboBoxMembroRevisorTecnico3;
    }

    public void setUtilArrayComboBoxCadastro(List<ComboBox<Cadastro>> utilArrayComboBoxCadastro) {
        this.utilArrayComboBoxCadastro = utilArrayComboBoxCadastro;
    }

    /***********************************************************************************/
    //Geters and Setters//

    public List<ComboBox<Cadastro>> getUtilArrayComboBoxCadastro() {
        return utilArrayComboBoxCadastro;
    }
    public void setConcursoId(Long concursoId) {
        this.concursoId = concursoId;
    }

    public RadioButtonGroup<String> getRadioNivelNumAlternativas() {
        return radioNivelNumAlternativas;
    }

    public void setRadioNivelNumAlternativas(RadioButtonGroup<String> radioNivelNumAlternativas) {
        this.radioNivelNumAlternativas = radioNivelNumAlternativas;
    }

    public MultiFileMemoryBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(MultiFileMemoryBuffer buffer) {
        this.buffer = buffer;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public ComboBox<Cadastro> getComboBoxMembroRevisorLinguagem() {
        return comboBoxMembroRevisorLinguagem;
    }

    public void setComboBoxMembroRevisorLinguagem(ComboBox<Cadastro> comboBoxMembroRevisorLinguagem) {
        this.comboBoxMembroRevisorLinguagem = comboBoxMembroRevisorLinguagem;
    }

    public ComboBox<Cadastro> getComboBoxMembroBancaQuestao() {
        return comboBoxMembroBancaQuestao;
    }

    public void setComboBoxMembroBancaQuestao(ComboBox<Cadastro> comboBoxMembroBancaQuestao) {
        this.comboBoxMembroBancaQuestao = comboBoxMembroBancaQuestao;
    }

    public ComboBox<Cadastro> getComboBoxMembroRevisorTecnico1() {
        return comboBoxMembroRevisorTecnico1;
    }

    public void setComboBoxMembroRevisorTecnico1(ComboBox<Cadastro> comboBoxMembroRevisorTecnico1) {
        this.comboBoxMembroRevisorTecnico1 = comboBoxMembroRevisorTecnico1;
    }

    public ComboBox<Cadastro> getComboBoxMembroRevisorTecnico2() {
        return comboBoxMembroRevisorTecnico2;
    }

    public void setComboBoxMembroRevisorTecnico2(ComboBox<Cadastro> comboBoxMembroRevisorTecnico2) {
        this.comboBoxMembroRevisorTecnico2 = comboBoxMembroRevisorTecnico2;
    }
    public Button getSalvarButton() {
        return salvarButton;
    }

    public void setSalvarButton(Button salvarButton) {
        this.salvarButton = salvarButton;
    }
    public Long getConcursoId(){
        return this.concursoId;
    }
    public CadastroRepository getCadastroRepository() {
        return cadastroRepository;
    }

    public void setCadastroRepository(CadastroRepository cadastroRepository) {
        this.cadastroRepository = cadastroRepository;
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



    public NovaProvaPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(NovaProvaPresenter presenter) {
        this.presenter = presenter;
    }


	public DatePicker getPrazo() {
		return prazo;
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
	
    public Concurso getConcurso() {
        return concurso;
    }

    public void setConcurso(Concurso concurso) {
        this.concurso = concurso;
    }
    
    public RadioButtonGroup<String> getRadioTipoProva() {
        return radioTipoProva;
    }

    public void setRadioTipoProva(RadioButtonGroup<String> radio) {
        this.radioTipoProva = radio;
    }
    
    public RadioButtonGroup<String> getRadioNivelProva() {
        return radioNivelProva;
    }

    public void setRadioNivelProva(RadioButtonGroup<String> radio) {
        this.radioNivelProva = radio;
    }



}
