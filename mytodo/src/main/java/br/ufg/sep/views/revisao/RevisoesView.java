package br.ufg.sep.views.revisao;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.security.AuthenticatedUser;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.revisao.presenter.RevisaoPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value="revisoes", layout = MainLayout.class)
@RolesAllowed({"PED","ADMIN","PROF"})
@PageTitle("Revisões")
public class RevisoesView extends VerticalLayout {
    private QuestaoRepository questaoRepository;
    private Grid<Questao> questoesGrid = new Grid<>(Questao.class,false);
    private RevisaoPresenter revisaoPresenter;

    private Button revisarButton ;

    private Button visualizarButton = new Button("Visualizar",new Icon(VaadinIcon.EYE));
    private HorizontalLayout opcoesGridLayout;
    public RevisoesView(QuestaoRepository questaoRepository, ProvaRepository provaRepository,
                        AuthenticatedUser authenticatedUser){
        this.questaoRepository = questaoRepository;
        iniciarGrid();
        iniciarButoes();

        opcoesGridLayout = new HorizontalLayout(revisarButton);


        this.revisaoPresenter = new RevisaoPresenter(this,questaoRepository,provaRepository,authenticatedUser);
        add(opcoesGridLayout,questoesGrid);
    }

    private void iniciarButoes() {
        revisarButton = new Button("Revisar",new Icon(VaadinIcon.CLIPBOARD_CHECK));
        revisarButton.setEnabled(false);

        visualizarButton = new Button("Visualziar", new Icon(VaadinIcon.EYE));
    }



    private void iniciarGrid() {
        questoesGrid = new Grid<>(Questao.class,false);
        questoesGrid.addColumn("enunciado").setHeader("Enunciado").setWidth("300px");

        questoesGrid.addColumn(LitRenderer.<Questao>of(
                "<p>${item.concurso}<p>"
        ).withProperty("concurso",questao -> questao.getProva().getConcurso().getNome())
        ).setHeader("Concurso");

        questoesGrid.addColumn(LitRenderer.<Questao>of(
                        "<p>${item.areaConhecimento}<p>"
                ).withProperty("areaConhecimento",questao -> questao.getProva().getAreaConhecimento())
        ).setHeader("Area de Conhecimento");

        questoesGrid.addColumn("subAreas").setHeader("Subárea(s)");
        questoesGrid.addColumn("nivelDificuldade").setHeader("Nível");
        questoesGrid.addColumn("state").setHeader("Status");

        questoesGrid.addColumn(LitRenderer.<Questao>of(
                        "<p>${item.tipo}<p>"
                ).withProperty("tipo",questao -> questao.getProva().getTipo().toString())
        ).setHeader("Tipo").setKey("tipo");

        questoesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }


    public HorizontalLayout getOpcoesGridLayout() {
        return opcoesGridLayout;
    }

    public void setOpcoesGridLayout(HorizontalLayout opcoesGridLayout) {
        this.opcoesGridLayout = opcoesGridLayout;
    }

    /*********GETTERS AND SETTERS***********/

    public RevisaoPresenter getRevisaoPresenter() {
        return revisaoPresenter;
    }

    public void setRevisaoPresenter(RevisaoPresenter revisaoPresenter) {
        this.revisaoPresenter = revisaoPresenter;
    }

    public Button getRevisarButton() {
        return revisarButton;
    }

    public void setRevisarButton(Button revisarButton) {
        this.revisarButton = revisarButton;
    }

    public Button getVisualizarButton() {
        return visualizarButton;
    }

    public void setVisualizarButton(Button visualizarButton) {
        this.visualizarButton = visualizarButton;
    }



    public QuestaoRepository getQuestaoRepository() {
        return questaoRepository;
    }

    public void setQuestaoRepository(QuestaoRepository questaoRepository) {
        this.questaoRepository = questaoRepository;
    }

    public Grid<Questao> getQuestoesGrid() {
        return questoesGrid;
    }

    public void setQuestoesGrid(Grid<Questao> questoesGrid) {
        this.questoesGrid = questoesGrid;
    }
}
