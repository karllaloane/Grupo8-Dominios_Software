package br.ufg.sep.views.revisao.presenter;

import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.entity.*;
import br.ufg.sep.state.stateImpl.Revisao2;
import br.ufg.sep.state.stateImpl.Revisao3;
import br.ufg.sep.state.stateImpl.RevisaoLinguagem;
import br.ufg.sep.views.revisao.RevisarQuestaoView;
import br.ufg.sep.views.revisao.RevisoesView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;

import javax.validation.constraints.Null;
import java.util.List;

public class RevisarQuestaoPresenter {
    Questao questao;
    Prova prova;
    Concurso concurso;
    QuestaoRepository questaoRepository;

    RevisarQuestaoView view;

    public RevisarQuestaoPresenter(RevisarQuestaoView view, QuestaoRepository questaoRepository){
        this.questaoRepository = questaoRepository;
        this.view = view;
        questao = view.getQuestaoSelecionada();
        prova = questao.getProva();
        concurso = prova.getConcurso();
        Integer x = 0;
            configBotoes();
            setConcursoInfo();
            setProvaInfo();
            setQuestaoInfo(x);

    }

    private void configBotoes() {
        boolean entrarDesaprov = true;
        boolean entrarAprov = true;
        //se for revisao 2
       if(questao.getState() instanceof  Revisao2){
           entrarAprov = false;
           view.getEnviarRevisao().addClickListener(e->{
               Revisao revisao = new Revisao();
               revisao.setItemAnalisado(view.getTopicosAnalisadosHashMap());
               revisao.setOrientacoes(view.getOrientacoesQuestao().getValue());
               if(questao.enviarParaRevisaoLinguagem(revisao)){
                   Notification notification = Notification.show("Questão para revisão de linguagem com sucesso");
                   notification.setPosition(Notification.Position.TOP_CENTER);
                   notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                   view.getQuestaoService().getRepository().save(questao);
               }
               view.getEnviarRevisao().getUI().ifPresent(ui -> ui.navigate(RevisoesView.class));
           });

       }
            //se for revisao 3
        if(questao.getState() instanceof  Revisao3){
            entrarAprov = false;
            view.getEnviarRevisao().addClickListener(e->{
                Revisao revisao = new Revisao();
                revisao.setItemAnalisado(view.getTopicosAnalisadosHashMap());
                revisao.setOrientacoes(view.getOrientacoesQuestao().getValue());
                if(questao.enviarParaRevisaoLinguagem(revisao)){
                    Notification notification = Notification.show("Questão para revisão de linguagem com sucesso");
                    notification.setPosition(Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    view.getQuestaoService().getRepository().save(questao);
                }
                view.getEnviarRevisao().getUI().ifPresent(ui -> ui.navigate(RevisoesView.class));
            });

            entrarDesaprov = false;
            view.getEnviarBanca().addClickListener(click->{
                Revisao revisao = new Revisao();
                revisao.setItemAnalisado(view.getTopicosAnalisadosHashMap());
                revisao.setOrientacoes(view.getOrientacoesQuestao().getValue());
               if(questao.descartar(revisao)){
                   Notification notification = Notification.show("Questão para revisão de linguagem com sucesso");
                   notification.setPosition(Notification.Position.TOP_CENTER);
                   notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                   view.getQuestaoService().getRepository().save(questao);
               }
                view.getEnviarRevisao().getUI().ifPresent(ui -> ui.navigate(RevisoesView.class));
            });

        }


        //botão Desaprovar questão
        if(entrarDesaprov)
        view.getEnviarBanca().addClickListener(click->{

                Revisao revisao = new Revisao();
                revisao.setItemAnalisado(view.getTopicosAnalisadosHashMap());
                revisao.setOrientacoes(view.getOrientacoesQuestao().getValue());
                if(questao.enviarParaBanca(revisao)){
                    Notification notification = Notification.show("Questão enviada para banca com sucesso");
                    notification.setPosition(Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    view.getQuestaoService().getRepository().save(questao);
                }
                view.getEnviarBanca().getUI().ifPresent(ui -> ui.navigate(RevisoesView.class));
        });

        //botão Aprovar questão
        if(entrarAprov)
        view.getEnviarRevisao().addClickListener(e->{
           if(questao.enviarParaRevisao(questao.getState().getCorrecao())){
               Notification notification = Notification.show("Questão enviada para revisão com sucesso");
               notification.setPosition(Notification.Position.TOP_CENTER);
               notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
               view.getQuestaoService().getRepository().save(questao);
           }
            view.getEnviarRevisao().getUI().ifPresent(ui -> ui.navigate(RevisoesView.class));
        });


    }

    private void setConcursoInfo(){
        view.getNomeConcurso().setValue(concurso.getNome());
        view.getCidadeConcurso().setValue(concurso.getCidade());
        view.getDataInicioConcurso().setValue(concurso.getDataInicio());
        view.getDataFimConcurso().setValue(concurso.getDataFim());
    }
    private void setProvaInfo() {
        view.getAreaConhecimento().setValue(prova.getAreaConhecimento());
        view.getTipoProva().setValue(prova.getTipo().toString());
        view.getNumAlternativas().setValue(
                prova.getTipo().equals(TipoProva.OBJETIVA_4) || prova.getTipo().equals(TipoProva.OBJETIVA_5) ?
                        prova.getTipo().equals(TipoProva.OBJETIVA_4) ?
                                "4"
                                :
                                "5"
                        :
                        ""
        );
        if(view.getNumAlternativas().getValue().equals(""))view.getNumAlternativas().setVisible(false);
        view.getNivelProva().setValue(prova.getNivel().toString());
        view.getPrazo().setValue(prova.getDataEntrega());
        view.getDescricaoDaProva().setValue(prova.getDescricao());
    }


    private void setQuestaoInfo(Integer x) throws NullPointerException{
        String subAreas = new String("");
        for(String sub : questao.getSubAreas()){
            subAreas+=sub+"\n";
        }

        view.getSubAreasQuestao().setValue(subAreas);
        view.getEnunciadoQuestao().setValue(questao.getEnunciado());
        view.getNivelDificuldadeQuestaoCombo().setValue(questao.getNivelDificuldade().toString());
        view.getEstadoQuestao().setValue(questao.getState().toString());
        List<TextArea> alternativas = List.of(
                view.getAlternativaAQuestao(),
                view.getAlternativaBQuestao(),
                view.getAlternativaCQuestao(),
                view.getAlternativaDQuestao(),
                view.getAlternativaEQuestao()
        );
        if(prova.getTipo().equals(TipoProva.OBJETIVA_4)){
            view.getJustificativaQuestao().setValue(questao.getJustificativa());
            for(int i=0;i<4;i++)
                    alternativas.get(i).setValue(((QuestaoObjetiva)questao).getAlternativas().get(i));
                alternativas.get(4).setVisible(false);
        }
        if(prova.getTipo().equals(TipoProva.OBJETIVA_5)){
            view.getJustificativaQuestao().setValue(questao.getJustificativa());
            for(int i=0;i<5;i++)
                alternativas.get(i).setValue(((QuestaoObjetiva)questao).getAlternativas().get(i));
        }
        if(prova.getTipo().equals(TipoProva.DISCUSSIVA)){
            view.getJustificativaQuestao().setVisible(false);
            alternativas.get(0).setLabel("Resposta Esperada");
            alternativas.get(0).setValue(
                    ((QuestaoDiscursiva)questao).getRespostaEsperada()
            );
            for(int i=1;i<5;i++)
                alternativas.get(i).setVisible(false);
        }
    }

}
