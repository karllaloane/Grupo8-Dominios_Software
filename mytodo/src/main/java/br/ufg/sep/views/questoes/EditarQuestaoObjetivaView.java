package br.ufg.sep.views.questoes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.componente.CancelarEdicaoDialog;
import br.ufg.sep.views.questoes.componente.ConfirmaEnvioRevisaoDialog;
import br.ufg.sep.views.questoes.componente.MetadadosQuestaoComponent;
import br.ufg.sep.views.questoes.presenter.EditarQuestaoObjetivaPresenter;
import br.ufg.sep.views.questoes.presenter.NovaQuestaoObjetivaPresenter;

@Route(value="editar_questao_objetiva", layout = MainLayout.class)
@PageTitle("Editar Questão Objetiva")
@PermitAll
public class EditarQuestaoObjetivaView extends VerticalLayout implements HasUrlParameter<Long>{

	private ProvaService provaService;
	private QuestaoService questaoService;
	private EditarQuestaoObjetivaPresenter presenter;
	
	//inputs gerais
	private TextField subareaTF;
	private ComboBox<NivelDificuldade> nivelDificuldadeCombo;
	private TextArea justificativaTA;
	
	//inputs da questao
	private TextArea enunciado;	
	private List<TextArea> alternativasList;
	private List<Checkbox> checkboxList;

	private Button salvarButton;
	private Button descartarButton;
	private Button enviarButton;

	//layouts final
	//todos eles são criados no construtor
	//e adicionados posteriormente
	private VerticalLayout layoutFinal; 
	private VerticalLayout alternativaLayout;
	private VerticalLayout justificativaLayout;
	private VerticalLayout buttonsLayout;
	
	private MetadadosQuestaoComponent metadados;
	private ConfirmaEnvioRevisaoDialog envioDialogo;
	private CancelarEdicaoDialog cancelarDialogo;

	private QuestaoObjetiva questaoObjetiva;
	private int quantAlternativas;

	public EditarQuestaoObjetivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		//criando os componentes
		setMetadados(new MetadadosQuestaoComponent());
		envioDialogo = new ConfirmaEnvioRevisaoDialog();
		cancelarDialogo = new CancelarEdicaoDialog();

		//criando os layouts intermediarios
		HorizontalLayout informacaoLayout = new HorizontalLayout();
		VerticalLayout enunciadoLayout = new VerticalLayout();
				
		//criando as listas que serao usadas pra guardar os componentes
		alternativasList = new ArrayList<>();
		checkboxList = new ArrayList<>();		
		
		//criando os layouts principais
		alternativaLayout = new VerticalLayout();
		layoutFinal = new VerticalLayout();
		justificativaLayout = new VerticalLayout();
		buttonsLayout = new VerticalLayout();
		
		//ajustando tamanho dos layouts
		enunciadoLayout.setWidth("667px");
		alternativaLayout.setWidth("700px");
		informacaoLayout.setWidth("700px");
		justificativaLayout.setWidth("699px");
		buttonsLayout.setWidth("699px");
		
		//criando os componentes de informacoes sobre a questao a ser cadastrada
		subareaTF = new TextField("Subárea da questão");
		
		//criação do combobx
		nivelDificuldadeCombo = new ComboBox<>("Nível de dificuldade");
		nivelDificuldadeCombo.setItems(EnumSet.allOf(NivelDificuldade.class));
		
		//alterando estilos
		subareaTF.setWidth("400px");
		//informacaoLayout.add(subareaTF, nivelDificuldadeCombo); //adicionando ao layout intermediario
		informacaoLayout.add(metadados);
		
		/**************** Layout do enunciado ***********************/
	
		//criando os componentes do layout de questao
		Label enunciadoLabel = new Label("Enunciado");
		enunciado = new TextArea();
		
		//alterando estilos
		enunciado.setWidthFull();
		enunciado.setMinHeight("150px");

		enunciadoLayout.add(enunciadoLabel, enunciado);
		
		//estilo do layout do enunciado
		enunciadoLayout.setSpacing(false);
		enunciadoLayout.setPadding(false);
		
		layoutFinal.add(informacaoLayout, enunciadoLayout);
		
		add(layoutFinal);
		
	}

	private void addAlternativas() {
	
		//Layout de Label
		Label alternativaLabel = new Label("Alternativas");
		Label corretaLabel = new Label("Correta");
			
		HorizontalLayout labelLayout = new HorizontalLayout();
		
		alternativaLabel.setWidth("597px");
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
			alternativasList.get(i).setWidth("570px");
			
			alternativasList.get(i).setValue(questaoObjetiva.getAlternativas().get(i));
			
			//cria o textbox e adiciona ao list
			checkboxList.add(new Checkbox());
			
			if(i == questaoObjetiva.getAlternativaCorreta())
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

	private void addJustificativa() {
		
		Label justificativaLabel = new Label("Justificativa da alternativa correta:");
		
		justificativaTA = new TextArea();
		justificativaTA.setWidthFull();
		
		justificativaTA.setValue(questaoObjetiva.getJustificativa());
		
		justificativaLayout.add(justificativaLabel, justificativaTA);
		
		justificativaLayout.setSpacing(false);
		
		add(justificativaLayout);
	}
	
	private void addBotoes() {
		//critando botoes
		
		HorizontalLayout h = new HorizontalLayout();
		
		this.descartarButton = new Button("Descartar edição");
		this.salvarButton = new Button("Salvar");
		this.enviarButton = new Button("Enviar para revisão 1");
		
		this.salvarButton.getStyle().set("margin-left", "247px");
		this.enviarButton.getStyle().set("margin-left", "auto");
		h.add(descartarButton, salvarButton,enviarButton);
		
		buttonsLayout.add(h);
		
		this.add(buttonsLayout);
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questaoObjetiva = (QuestaoObjetiva) optionalQuestao.get();	
			
			quantAlternativas = questaoObjetiva.getQuantAlternativas();
			
			//setando o valor dos campos criados no construtor
			//pode virar uma funcao mas fiquei com preguica
			enunciado.setValue(questaoObjetiva.getEnunciado());
			
			//setando os valores do componente de metadados
			metadados.setSubAreas(questaoObjetiva.getSubAreas());
			metadados.atualizaGrid(); //atualizando o grid apos setar a lista de subareas
			metadados.getNivelDificuldadeCombo().setValue(questaoObjetiva.getNivelDificuldade());
			
			//chama o método que adiciona o layout de alternativas de acordo com a quantidade de questoes
			//definidas no cadastro da prova
			addAlternativas();

			//chama o método para criar o resto do layout
			addJustificativa();
			addBotoes();

			this.presenter = new EditarQuestaoObjetivaPresenter(provaService, questaoService, this); //iniciar o presenter
		}
		
	}
	
	// Getters and setters *********************************************
	public List<TextArea> getAlternativasList() {
		return alternativasList;
	}
	
	public List<Checkbox> getCheckboxList() {
		return checkboxList;
	}
	
	public int getQuantAlternativas() {
		return quantAlternativas;
	}
	
	public Button getSalvarButton() {
		return salvarButton;
	}

	public Button getEnviarButton() {
		return enviarButton;
	}
	
	public Button getDescartarButton() {
		return descartarButton;
	}
	
	public TextField getSubareaTF() {
		return subareaTF;
	}

	public ComboBox<NivelDificuldade> getNivelDificuldadeCombo() {
		return nivelDificuldadeCombo;
	}

	public TextArea getJustificativaTA() {
		return justificativaTA;
	}

	public TextArea getEnunciado() {
		return enunciado;
	}
	
	public MetadadosQuestaoComponent getMetadados() {
		return metadados;
	}

	public void setMetadados(MetadadosQuestaoComponent metadados) {
		this.metadados = metadados;
	}
	
	public QuestaoObjetiva getQuestaoObjetiva() {
		return questaoObjetiva;
	}
	
	public ConfirmaEnvioRevisaoDialog getEnvioDialogo() {
		return envioDialogo;
	}

	public CancelarEdicaoDialog getCancelarDialogo() {
		return cancelarDialogo;
	}
}