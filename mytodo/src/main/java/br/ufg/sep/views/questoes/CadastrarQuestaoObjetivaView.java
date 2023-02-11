package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.presenter.VisualizarQuestoesProvaPresenter;

@Route(value="cadastrar_questoes_prova", layout = MainLayout.class)
@PageTitle("Cadastrar Questão")
@PermitAll
public class CadastrarQuestaoObjetivaView extends VerticalLayout implements HasUrlParameter<Long>{

	private ProvaService provaService;
	private QuestaoService questaoService;
	
	//inputs gerais
	private TextField subareaTF;
	private ComboBox<NivelDificuldade> nivelDificuldadeCombo;
	private TextArea justificativa;
	
	//inputs da questao
	private TextArea enunciado;
	private TextArea alternativa1;
	private TextArea alternativa2;
	private TextArea alternativa3;
	private TextArea alternativa4;
	private TextArea alternativa5;

	//layouts final
	private VerticalLayout layoutFinal; 

	private Prova prova;
	//private Long provaId;
	
	public CadastrarQuestaoObjetivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		layoutFinal = new VerticalLayout();
		
		criarTela();
		
		add(layoutFinal);
		
	}
	
	private void criarTela() {
		//criando os layouts intermediarios
		HorizontalLayout informacaoLayout = new HorizontalLayout();
		VerticalLayout alternativaLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		VerticalLayout enunciadoLayout = new VerticalLayout();
		
		//criando os componentes de informacoes sobre a questao a ser cadastrada
		subareaTF = new TextField("Subárea da questão");
		nivelDificuldadeCombo = new ComboBox<>("Nível de dificuldade");
		
		//alterando estilos
		subareaTF.setWidth("400px");
		informacaoLayout.setPadding(true);
		
		informacaoLayout.add(subareaTF, nivelDificuldadeCombo); //adicionando ao layout intermediario
		
		/**************** Layout do enunciado ***********************/
		
		//criando os componentes do layout de questao
		Label enunciadoLabel = new Label("Enunciado");
		enunciado = new TextArea();
		
		//alterando estilos
		enunciado.setWidth("700px");
		enunciado.setHeight("150px");

		enunciadoLayout.add(enunciadoLabel, enunciado);
		
		//estilo do layout do enunciado
		enunciadoLayout.setSpacing(false);
				
		
		/**************** Layout de questoes ***********************/
		
		//criandos os componentes
		Label alternativaLabel = new Label("Alternativas");
		
		alternativa1 = new TextArea("A)");
		alternativa2 = new TextArea("B)");
		alternativa3 = new TextArea("C)");
		alternativa4 = new TextArea("D)");
		alternativa5 = new TextArea("E)");
			
		alternativaLayout.add(alternativa1, alternativa2, alternativa3, alternativa4, alternativa5);
		alternativaLayout.setWidth("500px");
		alternativaLayout.setSpacing(false);
		
		alternativaLayout.getChildren().forEach(txtArea->{
        	((TextArea)txtArea).setWidthFull();
        	((TextArea)txtArea).setMaxHeight("90px");
        	
        });
		
		layoutFinal.add(informacaoLayout, enunciadoLayout, alternativaLayout);
	}


	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalQuestao = provaService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			prova = optionalQuestao.get();
			//this.provaId = prova.getId();
			
			//tornando a quinta alternativa falta caso a prova seja objetiva com 4 alternativas
			if(prova.getTipo() == TipoProva.OBJETIVA_4) {
				this.alternativa5.setVisible(false);
			}
				

			//this.presenter = new VisualizarQuestoesProvaPresenter(provaService, questaoService,this); //iniciar o presenter
		}
		
	}
	
}
