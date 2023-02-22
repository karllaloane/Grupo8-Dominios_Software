package br.ufg.sep.views.gerencia;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerencia.presenter.ConcursoPresenter;

@Route(value="concursos", layout = MainLayout.class)
@PageTitle("Concursos")
@RolesAllowed({"ADMIN", "PED"})
public class ConcursosView extends VerticalLayout{
	
	Button novoButton;
	Button editarButton;
	Button visualizarButton;
	Button acessarProvasButton;

	private Button deletarConcurso;
	Grid<Concurso> concursos;
	HorizontalLayout layout = new HorizontalLayout();
	
	public ConcursosView(SecurityService secutiryService, CadastroRepository cr, ConcursoService cS){
	
		criarButton(); // inicializa os botões


		concursos = new Grid<>(Concurso.class,false);
		ConcursoPresenter presenter = new ConcursoPresenter(this, cS);

		//editarButton.getElement().getStyle().set("margin-left", "auto");
        
        concursos.addColumn("nome").setAutoWidth(true);
		concursos.addColumn("cidade").setAutoWidth(true);
		concursos.addColumn(presenter.getRenderizadorDatasConcurso()).setAutoWidth(true).setHeader("Fim do edital");
		concursos.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        
        layout.add(novoButton, visualizarButton, editarButton, acessarProvasButton,deletarConcurso);
        
        add(layout,concursos);
	}

	public void habilitarButtons() { // método controlado pelo ConcursoPresenter
		editarButton.setEnabled(true);
		visualizarButton.setEnabled(true);
		acessarProvasButton.setEnabled(true);
		deletarConcurso.setEnabled(true);
	}

	//******** método para inicializar os botões ********
	private void criarButton() {
		novoButton = new Button("Novo", new Icon(VaadinIcon.PLUS));
		editarButton = new Button("Editar", new Icon(VaadinIcon.PENCIL));
		visualizarButton = new Button("Visualizar", new Icon(VaadinIcon.EYE));
		acessarProvasButton = new Button("Gerenciar Provas", new Icon(VaadinIcon.FILE_TEXT_O ));
		deletarConcurso = new Button("Deletar",new Icon(VaadinIcon.TRASH));
		deletarConcurso.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		deletarConcurso.setEnabled(false);
		editarButton.setEnabled(false);
		editarButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		visualizarButton.setEnabled(false);
		visualizarButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		acessarProvasButton.setEnabled(false);
		acessarProvasButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		layout = new HorizontalLayout();
		layout.setWidthFull();
        layout.setPadding(true);		
	}
	
	/******************************/
	/*Getters and Setters*/

	public Grid<Concurso> getGrid() {
		return concursos;
	}

	public void setGrid(Grid<Concurso> grid) {
		this.concursos = grid;
	}
	
	public Button getNovoButton() {
		return novoButton;
	}
	
	public Button getEditarButton() {
		return editarButton;
	}
	
	public Button getVisualizarButton() {
		return visualizarButton;
	}
	
	public Button getAcessarProvasButton() {
		return acessarProvasButton;
	}

	public void setNovoButton(Button novoButton) {
		this.novoButton = novoButton;
	}

	public void setEditarButton(Button editarButton) {
		this.editarButton = editarButton;
	}

	public void setVisualizarButton(Button visualizarButton) {
		this.visualizarButton = visualizarButton;
	}

	public void setAcessarProvasButton(Button acessarProvasButton) {
		this.acessarProvasButton = acessarProvasButton;
	}

	public Button getDeletarConcurso() {
		return deletarConcurso;
	}

	public void setDeletarConcurso(Button deletarConcurso) {
		this.deletarConcurso = deletarConcurso;
	}

	public Grid<Concurso> getConcursos() {
		return concursos;
	}

	public void setConcursos(Grid<Concurso> concursos) {
		this.concursos = concursos;
	}

	public HorizontalLayout getLayout() {
		return layout;
	}

	public void setLayout(HorizontalLayout layout) {
		this.layout = layout;
	}
}
