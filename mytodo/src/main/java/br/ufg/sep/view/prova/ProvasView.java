package br.ufg.sep.view.prova;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.view.prova.presenter.ProvasPresenter;
import br.ufg.sep.views.MainLayout;

@Route(value="provas", layout = MainLayout.class)
@PageTitle("Provas")
@PermitAll
public class ProvasView extends VerticalLayout{
	
	private Grid<Prova> provas;
	private Button visualizarButton;
	private HorizontalLayout layout;
	
	public ProvasView(SecurityService secutiryService, CadastroRepository cr, ProvaService provaService){
		
		iniciaGrid();
		
		//criando presenter
		ProvasPresenter presenter = new ProvasPresenter(this,provaService );
		
		//criacao do layout
		//botao
		layout = new HorizontalLayout();
		visualizarButton = new Button("Visualizar", new Icon(VaadinIcon.EYE));
		visualizarButton.setEnabled(false);
		visualizarButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		layout.add(visualizarButton);
		
		add(layout, provas);
	}
	
	//metodo para iniciar o grid, realizando as buscas ao banco
	private void iniciaGrid() {
		provas = new Grid<>(Prova.class,false);
		
		this.provas.addColumn(LitRenderer
				.<Prova>of("<b>${item.name}</b>").
				withProperty("name", prova -> prova.getConcurso().getNome())
						).setHeader("Concurso");
		provas.addColumn("areaConhecimento").setHeader("Área do Conhecimento");
		provas.addColumn("numeroQuestoes").setHeader("Questões solicitadas");
		//provas.addColumn("atividade").setHeader("Atividade");
//		this.provas.addColumn(LitRenderer
//				.<Prova>of("${item.name}</b>")
//				.withProperty("name", prova -> prova.getResponsavel().getNome())
//				).setHeader("Name");
//		provas.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
	}

	//habilita botao apos selecionar prova
	public void habilitarButtons() {
		this.visualizarButton.setEnabled(true);
	}
	
	public void setGrid(Grid<Prova> provas) {
		this.provas = provas;	
	}
	
	public Grid<Prova> getProvas() {
		return provas;
	}

}
