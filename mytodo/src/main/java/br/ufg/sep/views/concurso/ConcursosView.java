package br.ufg.sep.views.concurso;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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
	
	Grid<Concurso> concursos;
	public ConcursosView(SecurityService secutiryService, CadastroRepository cr, ConcursoService cS){
	
		ConcursoPresenter presenter = new ConcursoPresenter(this,cS);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidthFull();
        layout.setPadding(true);

        Button novoButton = new Button("Novo", new Icon(VaadinIcon.PLUS));
        //novoButton.getElement().getStyle().set("margin-left", "auto");
        
        novoButton.addClickListener(e->{
			 novoButton.getUI().ifPresent(ui->{
			 ui.navigate(FormularioConcursoView.class); });
		});
        
        layout.add(novoButton);
        
        
        add(layout,concursos);
	}

	public Grid<Concurso> getGrid() {
		return concursos;
	}

	public void setGrid(Grid<Concurso> grid) {
		this.concursos = grid;
	}

}
