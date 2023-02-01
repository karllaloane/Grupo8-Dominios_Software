package br.ufg.sep.views.concurso;

import javax.annotation.security.PermitAll;

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
import br.ufg.sep.views.concurso.presenter.ConcursoPresenter;

@Route(value="concursos", layout = MainLayout.class)
@PageTitle("Concursos")
@PermitAll
public class ConcursosView extends VerticalLayout{
	
	Button novoButton;
	Button editarButton;
	Button visualizarButton;
	Button acessarProvasButton;
	Grid<Concurso> concursos;
	HorizontalLayout layout;
	
	public ConcursosView(SecurityService secutiryService, CadastroRepository cr, ConcursoService cS){
	
		criarButton();
		
		concursos = new Grid<>(Concurso.class,false);
		
		ConcursoPresenter presenter = new ConcursoPresenter(this, cS);

		//editarButton.getElement().getStyle().set("margin-left", "auto");
        
        concursos.addColumn("nome").setAutoWidth(true);
		concursos.addColumn("cidade").setAutoWidth(true);
		concursos.addColumn("dataFim").setAutoWidth(true);
		concursos.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        
        layout.add(novoButton, visualizarButton, editarButton, acessarProvasButton);
        
        add(layout,concursos);
	}

	public void habilitarButtons() {
		editarButton.setEnabled(true);
		visualizarButton.setEnabled(true);
		acessarProvasButton.setEnabled(true);
	}
	
	private void criarButton() {
		novoButton = new Button("Novo", new Icon(VaadinIcon.PLUS));
		editarButton = new Button("Editar", new Icon(VaadinIcon.PENCIL));
		visualizarButton = new Button("Visualizar", new Icon(VaadinIcon.EYE));
		acessarProvasButton = new Button("Acessar Provas", new Icon(VaadinIcon.FILE_TEXT_O ));
		
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
		return visualizarButton;
	}

}
