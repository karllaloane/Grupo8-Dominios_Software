package br.ufg.sep.views.revisao.presenter;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.security.AuthenticatedUser;
import br.ufg.sep.state.QuestaoState;
import br.ufg.sep.state.stateImpl.*;
import br.ufg.sep.views.revisao.RevisaoLinguagemQuestaoView;
import br.ufg.sep.views.revisao.RevisarQuestaoView;
import br.ufg.sep.views.revisao.RevisoesView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;

import java.util.ArrayList;
import java.util.List;

public class RevisaoPresenter {
    QuestaoRepository questaoRepository;
    ProvaRepository provaRepository;

    Cadastro usuario;

    List<Prova> provasDoUsuario;

    List<Questao> questoesUsuario;

    RevisoesView view;

    Questao questaoSelecionada;

    public RevisaoPresenter(RevisoesView view,
                            QuestaoRepository questaoRepository,
                            ProvaRepository provaRepository,
                            AuthenticatedUser authenticatedUser){
        /***Inicializar atributos***/
        this.view = view;
        this.questaoRepository = questaoRepository;
        this.provaRepository = provaRepository;
        if(!authenticatedUser.get().isPresent())
            authenticatedUser.logout();
        this.usuario = authenticatedUser.get().get();
        /**************************/

        /* Configurar a View */
        this.questoesUsuario = montarListaQuestoes();
        configGrid();
        configBotoes();


    }

    private void configBotoes() {



        view.getRevisarButton().addClickListener(click -> {
            view.getRevisarButton().getUI().ifPresent(ui -> {
                if(questaoSelecionada.getState() instanceof  RevisaoLinguagem){
                    ui.navigate(RevisaoLinguagemQuestaoView.class, questaoSelecionada.getId());

                }else
                ui.navigate(RevisarQuestaoView.class, questaoSelecionada.getId());
            });
        });

    }

    public void configGrid() {
        view.getQuestoesGrid().setItems(this.questoesUsuario);
        view.getQuestoesGrid().setSortableColumns("state");
        //selection listener
        view.getQuestoesGrid().addSelectionListener(selection->{
            if(selection.getFirstSelectedItem().isPresent()){
                questaoSelecionada = selection.getFirstSelectedItem().get();
                QuestaoState questaoState = questaoSelecionada.getState();

                //se a questão for revisável , ela aparecerá para o pessoal
                if(!((questaoState instanceof Correcao1)
                || questaoState instanceof Correcao2
                || questaoState instanceof RevisaoBanca
                        ))
                view.getRevisarButton().setEnabled(true);

                view.getVisualizarButton().setEnabled(true);
                return;
            }
                view.getOpcoesGridLayout().getChildren().forEach(ch->{
                    if(ch instanceof Button)((Button)ch).setEnabled(false);
                });


        });
    }


    public List<Questao> montarListaQuestoes(){

        List<Questao> questoesRevisar = new ArrayList<>();



        List<Prova> provaR1 = provaRepository.findByRevisor1(usuario);
        List<Prova> provaR2 = provaRepository.findByRevisor2(usuario);
        List<Prova> provaR3 = provaRepository.findByRevisor3(usuario);
        List<Prova> provaRL = provaRepository.findByRevisorLinguagem(usuario);

        provaR1.forEach(prova->{
            prova.getQuestoes().forEach(questao -> {
                if(questao.getState() instanceof Revisao1)
                    questoesRevisar.add(questao);
            });
        });

        provaR2.forEach(prova->{
            prova.getQuestoes().forEach(questao -> {
                if(questao.getState() instanceof Revisao2)
                    questoesRevisar.add(questao);
            });
        });

        provaR3.forEach(prova->{
            prova.getQuestoes().forEach(questao -> {
                if(questao.getState() instanceof Revisao3)
                    questoesRevisar.add(questao);
            });
        });

        provaRL.forEach(prova->{
            prova.getQuestoes().forEach(questao -> {
                if(questao.getState() instanceof RevisaoLinguagem)
                    questoesRevisar.add(questao);
            });
        });




        return  questoesRevisar;
    }


}
