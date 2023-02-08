package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
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

        view.getProvas().setItems(
                view.getConcursoService().getRepository().findById(
                        view.getConcursoId()
                ).get().getProvas()
                /*query-> provaService.getRepository()
                .findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream()
                */
        );





        configBotoes();
    }

    private void configBotoes(){
        view.getNovo().addClickListener(e->{
            view.getNovo().getUI().ifPresent(ui->{
                ui.navigate(NovaProvaView.class, view.getConcursoId());
            });
        });

    }

}
