package br.ufg.sep.views.revisao;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Select;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.views.MainLayout;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

@Route(value="revisao_questao_banca", layout = MainLayout.class)
@PageTitle("Revisão da Banca")
@PermitAll
public class RevisaoBancaView extends VerticalLayout implements HasUrlParameter<Long>{

	private ProvaService provaService;
	private QuestaoService questaoService;
	
	private HorizontalLayout questoesLayoutFinal;
	private VerticalLayout aprovaLayout;
	
	private Details details;
	
	private Button voltarButton;
	private Button enviarButton;
	private Checkbox checkbox;
	
	
	private Questao questao;
	private int quantAlternativas = 4;
	
	
	private ComponenteQuestao questaoAnterior;
	private ComponenteQuestao questaoNova;
	
	
	public RevisaoBancaView(ProvaService provaService, QuestaoService questaoService) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		questoesLayoutFinal = new HorizontalLayout();
		aprovaLayout = new VerticalLayout();
		
		criaLayoutQuestoes();	
		
		criaAprovaLayout();
		
		this.add(questoesLayoutFinal, aprovaLayout);
	}


	private void criaAprovaLayout() {
		checkbox = new Checkbox();		
		checkbox.setLabel("Eu CONCORDO com a revisão da questão e autorizo sua publicação nas provas do"
				+ " Insituto Verbena/UFG.");
		checkbox.getStyle().set("font-size", "17px");
		//aprovaLayout.setAlignItems(Alignment.CENTER);
		enviarButton = new Button("Enviar questão");		
		voltarButton = new Button("Voltar");
		
		enviarButton.getStyle().set("margin-left", "600px");
		enviarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		HorizontalLayout h = new HorizontalLayout();
		h.add(voltarButton);
		h.add(enviarButton);
		
		aprovaLayout.add(checkbox, h	);
	}


	private void criaLayoutQuestoes() {
		questaoAnterior = new ComponenteQuestao();
		questaoAnterior.bloquearEdicao();
		
		questaoNova = new ComponenteQuestao();
		
		VerticalLayout layout1 = new VerticalLayout();
		VerticalLayout layout2 = new VerticalLayout();
		
		Span spanQuestao1 = new Span("Visualizar questão antes da revisão");
		//spanQuestao1.getStyle().set("font-size", "12px").set("font-weight", "bold");
		Span spanQuestao2 = new Span("Questão após Revisão de Linguagem");
		spanQuestao2.getStyle().set("font-size", "16px").set("font-weight", "bold");
		
		layout1.add(questaoAnterior);
		layout2.add(spanQuestao2, questaoNova);
		
		layout1.setAlignItems(Alignment.CENTER);
		//layout2.setAlignItems(Alignment.CENTER);
		
		layout2.setPadding(false);
		layout1.setPadding(false);
		
		details = new Details(spanQuestao1, layout1);
		
		questoesLayoutFinal.add(details);
		questoesLayoutFinal.add(layout2);
		
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		java.util.Optional<Questao> optionalQuestao = questaoService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			questao = optionalQuestao.get();
			
			if(questao instanceof QuestaoObjetiva) {
	
				questaoAnterior.ocutarRespostaEsperada();
				questaoNova.ocutarRespostaEsperada();	
				
				if(((QuestaoObjetiva)questao).getQuantAlternativas() == 4) {
					questaoAnterior.ocultarQuintaAlternativa();
					questaoNova.ocultarQuintaAlternativa();
				}
				
			} else {
				questaoAnterior.ocutarAlternativas();
				questaoNova.ocutarAlternativas();
			}
				
		}
		
	}
	
	public Button getVoltarButton
	() {
		return voltarButton;
	}


	public Button getEnviarButton() {
		return enviarButton;
	}


	public Checkbox getCheckbox() {
		return checkbox;
	}
	

}
