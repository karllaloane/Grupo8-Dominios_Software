package br.ufg.sep.views.correcao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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
import br.ufg.sep.state.stateImpl.Correcao1;
import br.ufg.sep.state.stateImpl.Correcao2;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.correcao.presenter.CorrecaoObjetivaBancaPresenter;
import br.ufg.sep.views.questoes.componente.CancelarEdicaoDialog;
import br.ufg.sep.views.questoes.componente.ConfirmaEnvioRevisaoDialog;

@Route(value="correcao_questao_discursiva", layout = MainLayout.class)
@PageTitle("Revisao da Banca")
@PermitAll
public class CorrecaoDiscursivaBancaView extends VerticalLayout implements HasUrlParameter<Long>{
	
	private VerticalLayout revisaoTecnicaLayout;
	private VerticalLayout revisaoBancaLayout;
	private VerticalLayout questaoLayout;
	private Grid<String> grid;
	private TextArea orientacoesTextField;
	private TextArea justificativaAtendimentoTA;
	private TextArea enunciadoTextField;
	
	private TextArea justificativaCorretaTA;
	private RadioButtonGroup<String> radioGroup;
	
	private List<String> topicosDeRevisao;
	
	private Button salvarButton;
	private Button descartarButton;
	private Button enviarButton;
	
	private ConfirmaEnvioRevisaoDialog envioDialogo;
	private CancelarEdicaoDialog cancelarDialogo;
	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private CorrecaoObjetivaBancaPresenter presenter;
	
	//inputs da questao
	private TextArea enunciado;	
	private TextArea respostaEsperadaTA;
	
	private QuestaoDiscursiva questao;
	private Prova prova;
	private int quantAlternativas;
	
	public CorrecaoDiscursivaBancaView(ProvaService provaService, QuestaoService questaoService) {
		
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
			questao = (QuestaoDiscursiva) optionalQuestao.get();
			
			prova = questao.getProva();
			
			criaLayoutRevisaoTecnica();
			criaLayoutRevisaoBanca();
			criaLayoutQuestao();
			criaBotoesLayout();
			
			//presenter = new CorrecaoObjetivaBancaPresenter(provaService, questaoService, this);
			
		} else {
			Notification notification = Notification
			        .show("Impossível acessar a questão");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}
	
	private void criaLayoutRevisaoTecnica() {
		revisaoTecnicaLayout = new VerticalLayout();
		
		VerticalLayout layoutGrid = new VerticalLayout();
		VerticalLayout orientGrid = new VerticalLayout();
		
		Span revSpan = new Span("");
		
		revSpan.getStyle().set("font-size", "18px").set("font-weight", "bold");
		
		if(questao.getState() instanceof Correcao1)
			revSpan.setText("Dados da Revisão Técnica 1");
		else if(questao.getState() instanceof Correcao2) 
			revSpan.setText("Dados da Revisão Técnica 2");
		else
			revSpan.setText("Dados da Revisão de Linguagem");
		
		Span orientacaoSpan = new Span("Orientações");
		orientacoesTextField = new TextArea();
		orientacoesTextField.setValue( questao.getState().getRevisao().getOrientacoes());
		orientacoesTextField.setWidthFull();
		orientacoesTextField.setReadOnly(true);
		
		Span itensSpan = new Span("Itens Avaliados");
		criarGrid();
		
		orientGrid.add(itensSpan, grid);
		layoutGrid.add(orientacaoSpan, orientacoesTextField);
		
		orientGrid.setSpacing(false);
		orientGrid.setPadding(false);
		orientGrid.getStyle().set("margin-bottom", "10px");
		layoutGrid.setSpacing(false);
		layoutGrid.setPadding(false);
		
		revisaoTecnicaLayout.setWidth("800px");
		revisaoTecnicaLayout.setAlignItems(Alignment.CENTER);
		revisaoTecnicaLayout.add(revSpan, orientGrid, layoutGrid);
		
		
		this.add(revisaoTecnicaLayout);

	}
	
	private void criaLayoutQuestao() {
		questaoLayout = new VerticalLayout();
		
		Span spanQuestao = new Span("Questão Reelaborada");
		spanQuestao.getStyle().set("font-size", "18px").set("font-weight", "bold");
				
		VerticalLayout enunciadoLayout = new VerticalLayout();
		VerticalLayout respostaLayout = new VerticalLayout();
		
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
		
		//criando os componentes do layout de questao
		Span respSpan = new Span("Resposta Esperada");
		
		respostaEsperadaTA = new TextArea();
		respostaEsperadaTA.setMinHeight("150px");
		respostaEsperadaTA.setWidthFull();
		respostaEsperadaTA.setValue(questao.getRespostaEsperada());
		
		respostaLayout.setPadding(false);
		respostaLayout.setSpacing(false);
		respostaLayout.add(respSpan, respostaEsperadaTA);
		
		questaoLayout.setWidth("800px");
		questaoLayout.setAlignItems(Alignment.CENTER);
		
		questaoLayout.add(enunciadoLayout, respostaLayout);
		
		this.add(questaoLayout);
		
		
	}
	
	private void criaBotoesLayout() {
		//critando botoes
		VerticalLayout buttonsLayout = new VerticalLayout();
		buttonsLayout.setWidth("800px");
		
		HorizontalLayout h = new HorizontalLayout();
		
		this.descartarButton = new Button("Descartar edição");
		this.salvarButton = new Button("Salvar");
		this.enviarButton = new Button("Enviar para revisão 2");
		
		this.salvarButton.getStyle().set("margin-left", "345px");
		this.enviarButton.getStyle().set("margin-left", "auto");
		h.add(descartarButton, salvarButton,enviarButton);
		
		buttonsLayout.add(h);
		
		this.add(buttonsLayout);
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
		revSpan.getStyle().set("font-size", "18px").set("font-weight", "bold");
		
		Span radioSpan = new Span("As sugestões foram:");
		radioSpan.getStyle().set("margin-right", "5px");
		radioGrid.add(radioSpan, radioGroup);
		
		Span justSpan = new Span("Justificativa");
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
		
		/* Arraylist de tópicos */ 
		topicosDeRevisao = new ArrayList<>();
		
		
		for(Map.Entry<String, Integer> pair : questao.getState().getRevisao().getItemAnalisado().entrySet()){
			topicosDeRevisao.add(pair.getKey());
		}
		
		grid = new Grid<String>();
		
		grid.setItems(topicosDeRevisao);
		grid.setAllRowsVisible(true);
		
		//adicionando a coluna
		//conterá o arraylist e o botão de remover
		grid.addColumn(item -> item).setWidth("290px").setFlexGrow(1);
		
		/*
		 * NAO SEI SETAR O ATENDIMENTO NO GRID
		 */
		grid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkAtende, item) -> {
                	checkAtende.setLabel("Atende");
                })).setWidth("100px").setFlexGrow(0).setKey("atende");
		
		grid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkAtendeParcialmente, item) -> {
                	checkAtendeParcialmente.setLabel("Atende Parcialmente");
                	/* Ajeitar o taamanho disso aqui */
                })).setWidth("160px").setFlexGrow(1).setKey("atende-parcialmente");
		
		grid.addColumn(
                new ComponentRenderer<>(Checkbox::new, (checkNaoAtende, item) -> {
                	checkNaoAtende.setLabel("Não Atende");
                })).setWidth("100px").setFlexGrow(1).setKey("nao-atende");
		
		
		
	}

	
	public Prova getProva() {
		return prova;
	}
	
	public Grid<String> getGrid() {
		return grid;
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

	public QuestaoDiscursiva getQuestao() {
		return questao;
	}
}
