package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Select;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
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
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.presenter.QuestoesProvaPresenter;

@Route(value="visualizar_questoes_prova", layout = MainLayout.class)
@PageTitle("Questões")
@PermitAll
public class QuestoesProvaView extends VerticalLayout implements HasUrlParameter<Long> {
	
	private ProvaService provaService;
	private QuestaoService questaoService;

	private Prova prova;

	private Long provaId;
	private Grid<Questao> questoesGrid;
	private QuestoesProvaPresenter presenter;
	
	private HorizontalLayout layoutButton;
	private HorizontalLayout infoProvaLayout;
	private Details details;
	
	/* TF em referencia ao componente textfield */
	private TextField concursoTF;
	private TextField areaConhecimentoTF;
	private TextField nivelTF;
	private TextArea descricaoTF;
	private TextField numQuestoesFeitasTF;
	private TextField numQuestoesTotalTF;
	private TextField tipoProvaTF;
	
	private Button novaQuestaoButton;
	private Button acessarButton;
	private Button editarButton;
	private Button excluirButton;
	private Button downloadButton;
	
	private Dialog dialog;
	private Button dialogDeletaButton;
	private Button dialogCancelaButton;
	
	//com.vaadin.flow.component.select.Select<String> select;
	
	public QuestoesProvaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		dialog = new Dialog();
		dialog.setHeaderTitle("Deletar questão");
		dialog.add("Deseja excluir esta questão permanentemente?");
		
		dialogDeletaButton = new Button("Excluir");
		dialogDeletaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
		dialogDeletaButton.getStyle().set("margin-right", "auto");
		
		dialogCancelaButton = new Button("Cancelar");
		dialogCancelaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		dialog.getFooter().add(dialogDeletaButton);
		dialog.getFooter().add(dialogCancelaButton);
			
		iniciaGrid();
		criarButtons();
		criarLayoutInfoProva(); //cria o layout com informacoes da prova
		
		add(details, layoutButton, questoesGrid);
	}

	//metodo para iniciar o grid
	private void iniciaGrid() {	
		
//		select = new com.vaadin.flow.component.select.Select<>();
//		select.setLabel("Status");
//		select.setItems("Em elaboração", "Em correção I", "Em correção II", 
//				"Em correção II", "Em Revisão I", "Em Revisão II");
		
		questoesGrid = new Grid<>(Questao.class,false);
		
		questoesGrid.addColumn("enunciado").setHeader("Enunciado");
		questoesGrid.addColumn("subAreas").setHeader("Subárea");
		questoesGrid.addColumn("nivelDificuldade").setHeader("Nível");
		questoesGrid.addColumn("state").setHeader("Status").setSortable(true);
		//Grid.Column<Questao> stateC = questoesGrid.addColumn("state");
		questoesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		
//		HeaderRow headerRow = questoesGrid.appendHeaderRow();
//		
//		headerRow.getCell(stateC).setComponent(select);
	}
	
	//metodo para criacao dos botoes
	private void criarButtons() {
		layoutButton = new HorizontalLayout();
		novaQuestaoButton = new Button("Cadastrar Questão", new Icon(VaadinIcon.PLUS));
		novaQuestaoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		
		acessarButton = new Button("Acessar", new Icon(VaadinIcon.EYE));
		acessarButton.setEnabled(false);
		
		editarButton = new Button("Editar", new Icon(VaadinIcon.PENCIL));
		editarButton.setVisible(false);
		editarButton.getStyle().set("margin-right", "auto");
		
		excluirButton = new Button("Excluir", new Icon(VaadinIcon.TRASH));
		excluirButton.setVisible(false);
		excluirButton.getStyle().set("margin-right", "auto");
		
		downloadButton = new Button("Baixar arquivo", new Icon(VaadinIcon.DOWNLOAD));
		
		layoutButton.add(novaQuestaoButton, acessarButton, editarButton, excluirButton);
	}

	//metodo para criar o layout superior com informacoes da prova
	private void criarLayoutInfoProva() {
		
		//layouts para organizacao dos componentes
		HorizontalLayout h = new HorizontalLayout();
		HorizontalLayout h2 = new HorizontalLayout();
		VerticalLayout infosForm = new VerticalLayout();
		VerticalLayout vertical = new VerticalLayout();
		VerticalLayout numQuestaoform = new VerticalLayout();
		
		vertical.setSpacing(false);
		vertical.setPadding(false);
		h.setPadding(false);
		h2.setPadding(false);
		numQuestaoform.setPadding(false);
		
		HorizontalLayout summary = new HorizontalLayout();
		summary.setSpacing(false);
		summary.add(new Text("Prova - Informações Gerais"));
		
		infoProvaLayout = new HorizontalLayout();

		//labels
		concursoTF = new TextField("Concurso", "", "");
		areaConhecimentoTF = new TextField("Area do conhecimento", "", "");
		nivelTF = new TextField("Nível", "", "");
		descricaoTF = new TextArea("Descrição", "", "");
		numQuestoesFeitasTF = new TextField("Questões Elaboradas", "", "");
		numQuestoesTotalTF = new TextField("Questões Solicitadas", "", "");
		tipoProvaTF = new TextField("Tipo de prova", "", "");
		
		//tamanho dos componentes
		h.setWidthFull();
		h2.setWidthFull();
		infosForm.setWidth("1700px");
		concursoTF.setWidthFull();
		areaConhecimentoTF.setWidthFull();
		descricaoTF.setWidthFull();
		descricaoTF.setHeight("133px");
		nivelTF.setWidthFull();
		tipoProvaTF.setWidthFull();
		
		numQuestoesFeitasTF.setReadOnly(true);
		numQuestoesTotalTF.setReadOnly(true);
		numQuestoesFeitasTF.setWidth("150px");
		numQuestoesTotalTF.setWidth("150px");
		
		numQuestoesTotalTF.getStyle().set("margin-bottom", "33px");
		
		//espacamento dos componentes
		infosForm.setSpacing(false);
		numQuestaoform.setSpacing(false);
		
		//adicionando componentes aos formularios
		numQuestaoform.add(numQuestoesFeitasTF, numQuestoesTotalTF, downloadButton);
		h.add(concursoTF, areaConhecimentoTF);
		h2.add(tipoProvaTF, nivelTF);
		vertical.add(h, h2);
		
		infosForm.add(vertical, descricaoTF);
		infosForm.setSpacing(false);
		
		/** Seta apenas leitura **/
//		numQuestaoform.getChildren().forEach(txtField->{
//        	((TextField)txtField).setReadOnly(true);
//        	((TextField)txtField).setWidth("150px");
//        });
		h.getChildren().forEach(txtField->{
        	((TextField)txtField).setReadOnly(true);
        });
		h2.getChildren().forEach(txtField->{
        	((TextField)txtField).setReadOnly(true);
        });
		descricaoTF.setReadOnly(true);
		
		//organizando os componentes
		infoProvaLayout.add(infosForm, numQuestaoform);
		infoProvaLayout.setAlignItems(Alignment.BASELINE);
		infoProvaLayout.setWidthFull();

		details = new Details(summary, infoProvaLayout);
		details.addThemeVariants(DetailsVariant.FILLED);
		details.setWidthFull();
		details.setMinWidth("700px");
		details.setOpened(true);
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalQuestao = provaService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			prova = optionalQuestao.get();
			this.provaId = prova.getId();
			
			setInfoProva(); //setar as informacoes sobre a prova
			
			this.presenter = new QuestoesProvaPresenter(provaService, questaoService,this); //iniciar o presenter
		}
	}
	
	public void habilitarBotoesQuestao() {
		this.acessarButton.setEnabled(true);
	}
	
	public void desabilitarBotoesQuestao() {
		this.acessarButton.setEnabled(false);
	}

	//setar Informações Gerais da Prova.
	public void setInfoProva(){
		this.concursoTF.setValue(prova.getConcurso().getNome());
		this.areaConhecimentoTF.setValue(prova.getAreaConhecimento());
		this.descricaoTF.setValue(prova.getDescricao());
		this.numQuestoesTotalTF.setValue("" + prova.getNumeroQuestoes());
		this.nivelTF.setValue("" + prova.getNivel().toString());
		this.numQuestoesFeitasTF.setValue(String.format("%d",prova.getQuestoes().size()));
		
		if(prova.getTipo() == TipoProva.OBJETIVA_4){
			tipoProvaTF.setValue(prova.getTipo().toString() + " - 4 alternativas");
		}else if(prova.getTipo() == TipoProva.OBJETIVA_5){
			tipoProvaTF.setValue(prova.getTipo().toString() + " - 5 alternativas");
		} else {
			tipoProvaTF.setValue(prova.getTipo().toString());
		}
		
	}
	// --------------- Getter e Setters
	
	public Button getNovaQuestaoButton() {
		return novaQuestaoButton;
	}
	
	public Grid<Questao> getQuestoesGrid() {
		return questoesGrid;
	}


	public void setQuestoesGrid(Grid<Questao> questoes) {
		this.questoesGrid = questoes;
	}
	
	public QuestaoService getQuestaoService() {
		return questaoService;
	}

	public Long getProvaId() {
		return provaId;
	}
	
	public Button getAcessarButton() {
		return acessarButton;
	}
	
	public Prova getProva() {
		return prova;
	}
	
	public Button getEditarButton() {
		return editarButton;
	}

	public Button getExcluirButton() {
		return excluirButton;
	}
	
	public Button getDialogDeletaButton() {
		return dialogDeletaButton;
	}

	public Button getDialogCancelaButton() {
		return dialogCancelaButton;
	}	

	public Dialog getDialog() {
		return dialog;
	}

}
