package br.ufg.sep.views.correcao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.state.stateImpl.Correcao1;
import br.ufg.sep.state.stateImpl.Correcao2;
import br.ufg.sep.views.MainLayout;

@Route(value="correcao_questao_objetiva", layout = MainLayout.class)
@PageTitle("Revisao da Banca")
@PermitAll
public class CorrecaoObjetivaBancaView extends VerticalLayout implements HasUrlParameter<Long>{

	private VerticalLayout revisaoTecnicaLayout;
	private VerticalLayout revisaoBancaLayout;
	private Grid<String> grid;
	private TextField orientacoesTextField;
	private TextArea justificativaTextArea;
	private RadioButtonGroup<String> radioGroup;
	
	private List<String> topicosDeRevisao;
	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private QuestaoObjetiva questao;
	
	public CorrecaoObjetivaBancaView(ProvaService provaService, QuestaoService questaoService) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		add();
		
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questao = (QuestaoObjetiva) optionalQuestao.get();
			
			criaLayoutRevisaoTecnica();
			criaLayoutRevisaoBanca();
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
		orientacoesTextField = new TextField();
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
	
	private void criaLayoutRevisaoBanca() {
		revisaoBancaLayout = new VerticalLayout();
		
		radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("Atendidas totalmente", "Atendidas Parcialmente");
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
		
		Span radioSpan = new Span("As sugestões da banca foram:");
		radioSpan.getStyle().set("margin-right", "15px");
		radioGrid.add(radioSpan, radioGroup);
		
		Span justSpan = new Span("Justificativa");
		//justSpan.getStyle().set("font-weight", "bold");
		
		justificativaTextArea = new TextArea();
		justificativaTextArea.setWidthFull();
		
		justificativaGrid.add(justSpan, justificativaTextArea);
		
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

}
