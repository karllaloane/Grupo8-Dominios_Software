package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.componente.CancelarEdicaoDialog;
import br.ufg.sep.views.questoes.componente.ConfirmaEnvioRevisaoDialog;
import br.ufg.sep.views.questoes.componente.MetadadosQuestaoComponent;
import br.ufg.sep.views.questoes.presenter.NovaQuestaoDiscursivaPresenter;

@Route(value="cadastrar_questoes_discursiva", layout = MainLayout.class)
@PageTitle("Cadastrar Questão Discursiva")
@PermitAll
public class NovaQuestaoDiscursivaView extends VerticalLayout implements HasUrlParameter<Long>{
	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private NovaQuestaoDiscursivaPresenter presenter;
	
	private MetadadosQuestaoComponent metadados;
	private ConfirmaEnvioRevisaoDialog envioDialogo;
	private CancelarEdicaoDialog cancelarDialogo;
	
	//layouts final
	//todos eles são criados no construtor
	//e adicionados posteriormente
	private VerticalLayout layoutFinal; 
	private VerticalLayout buttonsLayout;
	
	//inputs da questao
	private TextArea enunciado;	
	private TextArea respostaEsperada;	
	
	private Button salvarButton;
	private Button enviarButton;
	private Button descartarButton;
	
	private Prova prova;
	
	public NovaQuestaoDiscursivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		//criando os componentes
		metadados = new MetadadosQuestaoComponent();
		envioDialogo = new ConfirmaEnvioRevisaoDialog();
		cancelarDialogo = new CancelarEdicaoDialog();
		
		//criando os layouts intermediarios
		HorizontalLayout informacaoLayout = new HorizontalLayout();
		VerticalLayout enunciadoLayout = new VerticalLayout();
		VerticalLayout respostaEsperadaLayout = new VerticalLayout();
		
		//criando os layouts principais
		layoutFinal = new VerticalLayout();
		buttonsLayout = new VerticalLayout();
		
		//ajustando tamanho dos layouts
		enunciadoLayout.setWidth("667px");
		informacaoLayout.setWidth("700px");
		respostaEsperadaLayout.setWidth("667px");
		buttonsLayout.setWidth("685px");
		
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
		
		/**************** Layout da resposta esperada ***********************/
		
		//criando os componentes
		Label labelRespEsperada = new Label("Resposta Esperada");
		respostaEsperada = new TextArea();
		
		//alterando estilos
		respostaEsperada.setWidthFull();
		respostaEsperada.setMinHeight("150px");
		
		respostaEsperadaLayout.add(labelRespEsperada, respostaEsperada);
		
		//estilo do layout do enunciado
		respostaEsperadaLayout.setSpacing(false);
		respostaEsperadaLayout.setPadding(false);
		
		addBotoes();
		
		layoutFinal.add(informacaoLayout, enunciadoLayout, respostaEsperadaLayout, buttonsLayout);
		
		add(layoutFinal);
	}
	
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalProva = provaService.getRepository().findById(parameter);
		if (optionalProva.isPresent()) {
			setProva(optionalProva.get());
			//this.provaId = prova.getId();
			
			

			this.presenter = new NovaQuestaoDiscursivaPresenter(provaService, questaoService, this); //iniciar o presenter
		}
		
	}

	private void addBotoes() {
		//critando botoes
		
		HorizontalLayout h = new HorizontalLayout();
		
		this.descartarButton = new Button("Descartar edição");
		this.salvarButton = new Button("Salvar");
		this.enviarButton = new Button("Enviar para revisão técnica");
		
		this.salvarButton.getStyle().set("margin-left", "205px");
		this.enviarButton.getStyle().set("margin-left", "auto");
		h.add(descartarButton, salvarButton,enviarButton);
		
		buttonsLayout.setPadding(false);
		
		buttonsLayout.add(h);
		
		this.add(buttonsLayout);
	}

	public Prova getProva() {
		return prova;
	}

	public void setProva(Prova prova) {
		this.prova = prova;
	}
	
	public Button getSalvarButton() {
		return salvarButton;
	}

	public Button getEnviarButton() {
		return enviarButton;
	}
	
	public Button getCancelarButton() {
		return descartarButton;
	}
	
	public MetadadosQuestaoComponent getMetadados() {
		return metadados;
	}

	public TextArea getEnunciado() {
		return enunciado;
	}
	
	public TextArea getRespostaEsperada() {
		return respostaEsperada;
	}

	public ConfirmaEnvioRevisaoDialog getEnvioDialogo() {
		return envioDialogo;
	}

	public CancelarEdicaoDialog getCancelarDialogo() {
		return cancelarDialogo;
	}
}
