package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
import br.ufg.sep.views.gerenciarProvas.VisualizarProvaView;
import br.ufg.sep.views.gerenciarProvas.EditarProvaView;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import java.util.Optional;

public class GerenciarProvasPresenter {

    private ProvaService provaService;
    private GerenciarProvasView view;
    
    private Prova provaSelecionada;
    
    public GerenciarProvasPresenter(ProvaService provaService, GerenciarProvasView view){
        this.view = view;
        this.provaService = provaService;
    
        //Adicionar um escutador de eventos de seleção de item na grid
        //Ele pega o item selecionado, o guarda em 'provaSelecionada' e habilita os botões.
        view.getProvas().addSelectionListener(selection -> {
            Optional<Prova> optionalProva = selection.getFirstSelectedItem();
            if (optionalProva.isPresent()) {
                Long testeId = optionalProva.get().getId();
                Optional<Prova> talvezProva = provaService.getRepository().findById(testeId);
                if(talvezProva.isPresent()) {
                    provaSelecionada = talvezProva.get();
                    view.habilitarButtons();
                }
            }
        });
        setarGrid();

        configBotoes();
    }

    private void setarGrid(){
        view.getProvas().setItems(
                view.getConcursoService().getRepository().findById(
                        view.getConcursoId()
                ).get().getProvas()
                /*query-> provaService.getRepository()
                .findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream()
                */
        );

    }

    private void configBotoes(){
    	
    	 
        view.getNovo().addClickListener(e->{
            view.getNovo().getUI().ifPresent(ui->{
                ui.navigate(NovaProvaView.class, view.getConcursoId());
            });
        });
        
        view.getEditar().addClickListener(e->{
            view.getEditar().getUI().ifPresent(ui->{
                ui.navigate(EditarProvaView.class,provaSelecionada.getId());
            });
        });
        
        view.getVisualizar().addClickListener(e->{
            view.getVisualizar().getUI().ifPresent(ui->{
                ui.navigate(VisualizarProvaView.class, provaSelecionada.getId());
            });
        });

        view.getDeletar().addClickListener(e->{
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setText("Você tem certeza de que deseja deletar essa prova?");
            confirmDialog.setConfirmText("Sim");
            confirmDialog.addConfirmListener(confirm->{
                this.provaService.getRepository().deleteById(provaSelecionada.getId());
                setarGrid();

            });
            confirmDialog.setRejectable(true);
            confirmDialog.setRejectText("Não");

            if(this.provaSelecionada.getQuestoes().size()<1){
                confirmDialog.open();
                return;
            }
            confirmDialog.setHeader("Existem questões associadas a essa prova");
            confirmDialog.setText("Você tem certeza de que deseja deletá-la?");

            confirmDialog.open();
        });

    }

}
