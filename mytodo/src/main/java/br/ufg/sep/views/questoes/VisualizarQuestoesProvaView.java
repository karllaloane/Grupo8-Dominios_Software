package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerenciarProvas.presenter.GerenciarProvasPresenter;
import br.ufg.sep.views.questoes.presenter.VisualizarQuestoesProvaPresenter;

@Route(value="visualizar_questoes_prova", layout = MainLayout.class)
@PageTitle("Questões")
@PermitAll
public class VisualizarQuestoesProvaView extends VerticalLayout implements HasUrlParameter<Long> {
	
	private ProvaService provaService;
	private QuestaoService questaoService;

	private Prova prova;
	private Long provaId;
	private Grid<Questao> questoesGrid;
	private HorizontalLayout layout;

	private VisualizarQuestoesProvaPresenter presenter;
	
	private Button novaQuestaoButton;
	

	public VisualizarQuestoesProvaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		iniciaGrid();
		criarButtons();
		layout.add(novaQuestaoButton);
				
		add(novaQuestaoButton, questoesGrid);
	}

	//metodo para iniciar o grid
	private void iniciaGrid() {
		questoesGrid = new Grid<>(Questao.class,false);
		
		questoesGrid.addColumn("enunciado").setHeader("Enunciado");
		questoesGrid.addColumn("conteudoEspecifico").setHeader("Subárea");
		questoesGrid.addColumn("nivelDificuldade").setHeader("Nível");
		//provas.addColumn("atividade").setHeader("Atividade");
		questoesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
	}
	
	//metodo para criacao dos botoes
	private void criarButtons() {
		layout = new HorizontalLayout();
		novaQuestaoButton = new Button("Cadastrar Questão", new Icon(VaadinIcon.PLUS));
		novaQuestaoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalQuestao = provaService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			prova = optionalQuestao.get();
			this.provaId = prova.getId();
			this.presenter = new VisualizarQuestoesProvaPresenter(provaService, questaoService,this); //iniciar o presenter
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

}
