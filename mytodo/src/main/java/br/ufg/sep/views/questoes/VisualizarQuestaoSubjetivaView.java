package br.ufg.sep.views.questoes;

import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.questoes.presenter.NovaQuestaoDiscursivaPresenter;

public class VisualizarQuestaoSubjetivaView extends VerticalLayout implements HasUrlParameter<Long>{

	
	private ProvaService provaService;
	private QuestaoService questaoService;
	private NovaQuestaoDiscursivaPresenter presenter;
	
	private MetadadosQuestaoComponent metadados;
	
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
	
	private Prova prova;
	private Questao questaoDiscursiva;
	
	public VisualizarQuestaoSubjetivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		metadados = new MetadadosQuestaoComponent();
		
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
		Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questaoDiscursiva = (QuestaoDiscursiva) optionalQuestao.get();	
			
		} else {
			Notification notification = Notification
			        .show("Impossível acessar a questão");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}

	private void addBotoes() {
		//critando botoes
		
		HorizontalLayout h = new HorizontalLayout();
		
		salvarButton = new Button("Salvar");
		
		//este botão poderá sofrer mudanças de acordo com o status da questão
		enviarButton = new Button("Enviar para Revisão Técnica 1");
		
		h.add(salvarButton, enviarButton);
		//h.setAlignItems(Alignment.END);
		
		buttonsLayout.add(h);
		buttonsLayout.setAlignItems(Alignment.END);
	}
}
