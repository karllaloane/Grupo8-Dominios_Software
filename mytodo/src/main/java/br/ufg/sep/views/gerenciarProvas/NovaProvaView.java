package br.ufg.sep.views.gerenciarProvas;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerenciarProvas.presenter.NovaProvaPresenter;
import br.ufg.sep.views.permissoes.GridCadastroFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value = "nova-prova", layout = MainLayout.class)
@PageTitle("Nova Prova")
@RolesAllowed({"ADMIN","PROF"})
public class NovaProvaView extends VerticalLayout implements HasUrlParameter<Long> {
    private Long concursoId; // só é instanciado após o construtor. (Só deve ser usado em Listeners)
    private ProvaService provaService;
    private ConcursoService concursoService;

    private CadastroRepository cadastroRepository;
    private TextField nomeConcurso = new TextField();

    private TextField areaConhecimento = new TextField();

    private TextField numQuestoes = new TextField();

    private TextField colaboradorAssociado = new TextField();

    private NovaProvaPresenter presenter;

    private Grid<Cadastro> elaboradoresGrid;

    private Grid<Cadastro> revisor1Grid;

    private Grid<Cadastro> revisor2Grid;

    private Button salvarButton = new Button("Salvar"); // Btn: Button


    public NovaProvaView(ProvaService provaService, ConcursoService concursoService,
                         CadastroRepository cadastroRepository){
        this.cadastroRepository = cadastroRepository;
        this.provaService = provaService;
        this.concursoService = concursoService;
        this.setAlignItems(Alignment.CENTER); // Alinhar a NovaProvaView no geral
        nomeConcurso.setLabel("Concurso pertencente");
        nomeConcurso.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); // Alinhar o texto dentro do nomeConcurso
        nomeConcurso.setReadOnly(true);
        nomeConcurso.setWidth("500px");

        areaConhecimento.setLabel("Area de conhecimento");

        numQuestoes.setLabel("Numero de questoes");

        colaboradorAssociado.setLabel("Colaborador associado");
        colaboradorAssociado.setReadOnly(true);
        colaboradorAssociado.setWidth("500px");

        HorizontalLayout contatinterCima = new HorizontalLayout(areaConhecimento,numQuestoes);

        elaboradoresGrid = new GridCadastroFactory(cadastroRepository).getGrid();
        elaboradoresGrid.setHeight("300px");
        elaboradoresGrid.setWidth("500px");



        this.presenter = new NovaProvaPresenter( this);
        add(nomeConcurso,contatinterCima,colaboradorAssociado, elaboradoresGrid,salvarButton);
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) { //Método executado após o construtor.

        concursoId = parameter;
        nomeConcurso.setValue(
                concursoService.getRepository().findById(parameter).get()
                        .getNome());

    }
    /***********************************************************************************/
    //Geters and Setters//
    public Button getSalvarButton() {
        return salvarButton;
    }

    public void setSalvarButton(Button salvarButton) {
        this.salvarButton = salvarButton;
    }
    public Long getConcursoId(){
        return this.concursoId;
    }
    public CadastroRepository getCadastroRepository() {
        return cadastroRepository;
    }

    public void setCadastroRepository(CadastroRepository cadastroRepository) {
        this.cadastroRepository = cadastroRepository;
    }


    public ProvaService getProvaService() {
        return provaService;
    }

    public void setProvaService(ProvaService provaService) {
        this.provaService = provaService;
    }

    public ConcursoService getConcursoService() {
        return concursoService;
    }

    public void setConcursoService(ConcursoService concursoService) {
        this.concursoService = concursoService;
    }

    public TextField getNomeConcurso() {
        return nomeConcurso;
    }

    public void setNomeConcurso(TextField nomeConcurso) {
        this.nomeConcurso = nomeConcurso;
    }

    public TextField getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(TextField areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public TextField getNumQuestoes() {
        return numQuestoes;
    }

    public void setNumQuestoes(TextField numQuestoes) {
        this.numQuestoes = numQuestoes;
    }

    public TextField getColaboradorAssociado() {
        return colaboradorAssociado;
    }

    public void setColaboradorAssociado(TextField colaboradorAssociado) {
        this.colaboradorAssociado = colaboradorAssociado;
    }

    public NovaProvaPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(NovaProvaPresenter presenter) {
        this.presenter = presenter;
    }

    public Grid<Cadastro> getElaboradoresGrid() {
        return elaboradoresGrid;
    }

    public void setElaboradoresGrid(Grid<Cadastro> elaboradoresGrid) {
        this.elaboradoresGrid = elaboradoresGrid;
    }


}
