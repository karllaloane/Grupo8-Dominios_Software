package br.ufg.sep.views.gerenciarProvas;

import javax.annotation.security.RolesAllowed;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.gerenciarProvas.presenter.GerenciarProvasPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.MainLayout;

import java.util.Optional;


@Route(value="gerenciar", layout=MainLayout.class)
@PageTitle("Gerenciar Provas")
@RolesAllowed({"ADMIN","PED"})
public class GerenciarProvasView extends VerticalLayout implements HasUrlParameter<Long>{

	public Long getConcursoId() {
		return concursoId;
	}

	public void setConcursoId(Long concursoId) {
		this.concursoId = concursoId;
	}

	private Long concursoId;
	private Concurso concurso;
	private ConcursoService concursoService;
	private Grid<Prova> provas = new Grid<>(Prova.class,false);
	private GerenciarProvasPresenter presenter;//presenter fora do construtor, para que tenha escopo por toda classe
	private ProvaService provaService;
	private Button novo;
	private Button editar;
	private Button visualizar;
	private HorizontalLayout provasOptionsLayout;
	
	
	public GerenciarProvasView(ProvaService provaService, ConcursoService concursoService) {
		this.concursoService = concursoService;
		this.provaService = provaService;
		iniciarGrid();
		iniciarBotoes(); // instanciar e editar o front end deles

		provasOptionsLayout = new HorizontalLayout(novo,editar,visualizar); // layout dos botões em cima da grid


		add(provasOptionsLayout,provas);
	}
	
	/* Grid de provas com os campos: Area de conhecimento, numero de questões e nome do colaborador responsável*/ 
	private void iniciarGrid() {
	this.provas.addColumn("areaConhecimento").setHeader("Area de Conhecimento");
	this.provas.addColumn("numeroQuestoes").setHeader("Numero de questoes");
		this.provas.addColumn(LitRenderer
				.<Prova>of("<b>${item.name}</b>")
				.withProperty("name", prova -> prova.getElaborador().getNome())
		).setHeader("Name");
	}

	/* Inicialização dos botões */ 
	private void iniciarBotoes(){
		novo = new Button("Nova prova",new Icon(VaadinIcon.PLUS));
		editar = new Button("Editar", new Icon(VaadinIcon.PENCIL));
		visualizar = new Button("Visualizar", new Icon(VaadinIcon.EYE));
		editar.setEnabled(false);
		visualizar.setEnabled(false);
	}


	public ConcursoService getConcursoService() {
		return concursoService;
	}

	public void setConcursoService(ConcursoService concursoService) {
		this.concursoService = concursoService;
	}

	public HorizontalLayout getProvasOptionsLayout() {
		return provasOptionsLayout;
	}

	public void setProvasOptionsLayout(HorizontalLayout provasOptionsLayout) {
		this.provasOptionsLayout = provasOptionsLayout;
	}


	public void habilitarButtons(){ // controlado pelo presenter
	this.provasOptionsLayout.getChildren().forEach(e->{
		((Button)e).setEnabled(true);
	});


	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
	Optional<Concurso> talvezConcurso= concursoService.getRepository().findById(parameter);
	if(talvezConcurso.isPresent())concurso = talvezConcurso.get();
		this.concursoId = parameter;
		this.presenter = new GerenciarProvasPresenter(provaService,this); //iniciar o presenter

	}


	/*************************************************/
	// Getters and Setters

	public Grid<Prova> getProvas() {
		return provas;
	}

	public void setProvas(Grid<Prova> provas) {
		this.provas = provas;
	}

	public GerenciarProvasPresenter getPresenter() {
		return presenter;
	}

	public void setPresenter(GerenciarProvasPresenter presenter) {
		this.presenter = presenter;
	}

	public ProvaService getProvaService() {
		return provaService;
	}

	public void setProvaService(ProvaService provaService) {
		this.provaService = provaService;
	}

	public Button getNovo() {
		return novo;
	}

	public void setNovo(Button novo) {
		this.novo = novo;
	}

	public Button getEditar() {
		return editar;
	}

	public void setEditar(Button editar) {
		this.editar = editar;
	}

	public Button getVisualizar() {
		return visualizar;
	}

	public void setVisualizar(Button visualizar) {
		this.visualizar = visualizar;
	}
	
	public Concurso getConcurso() {
		return concurso;
	}

	public void setConcurso(Concurso concurso) {
		this.concurso = concurso;
	}









}
