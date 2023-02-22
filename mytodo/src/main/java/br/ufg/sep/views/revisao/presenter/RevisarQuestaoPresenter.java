package br.ufg.sep.views.revisao.presenter;

import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.entity.*;
import br.ufg.sep.views.revisao.RevisarQuestaoView;
import br.ufg.sep.views.revisao.RevisoesView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;

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
        configBotoes();
        setConcursoInfo();
        setProvaInfo();
        setQuestaoInfo();
    }

    private void configBotoes() {

        //botão Desaprovar questão
        view.getEnviarBanca().addClickListener(click->{

                Revisao revisao = new Revisao();
                revisao.setItemAnalisado(view.getTopicosAnalisadosHashMap());
                revisao.setOrientacoes(view.getOrientacoesQuestao().getValue());
                if(questao.enviarParaBanca(revisao)){
                    Notification notification = Notification.show("Questão enviada com sucesso");
                    notification.setPosition(Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    view.getQuestaoService().getRepository().save(questao);
                }
                view.getEnviarBanca().getUI().ifPresent(ui -> ui.navigate(RevisoesView.class));
        });

        view.getEnviarRevisao().addClickListener(e->{
           if(questao.enviarParaRevisao(null)){
               Notification notification = Notification.show("Questão enviada com sucesso");
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
        view.getNivelProva().setValue(prova.getNivel().toString());
        view.getPrazo().setValue(prova.getDataEntrega());
        view.getDescricaoDaProva().setValue(prova.getDescricao());
    }


    private void setQuestaoInfo(){
        String subAreas = new String("");
        for(String sub : questao.getSubAreas()){
            subAreas+=sub+"\n";
        }
        view.getSubAreasQuestao().setValue(subAreas);
        view.getEnunciadoQuestao().setValue(questao.getEnunciado());
        view.getNivelDificuldadeQuestaoCombo().setValue(questao.getNivelDificuldade().toString());
        view.getJustificativaQuestao().setValue(questao.getJustificativa());
        view.getEstadoQuestao().setValue(questao.getState().toString());
        List<TextArea> alternativas = List.of(
                view.getAlternativaAQuestao(),
                view.getAlternativaBQuestao(),
                view.getAlternativaCQuestao(),
                view.getAlternativaDQuestao(),
                view.getAlternativaEQuestao()
        );
        if(prova.getTipo().equals(TipoProva.OBJETIVA_4)){
                for(int i=0;i<4;i++)
                    alternativas.get(i).setValue(((QuestaoObjetiva)questao).getAlternativas().get(i));
                alternativas.get(4).setVisible(false);
        }
        if(prova.getTipo().equals(TipoProva.OBJETIVA_5)){
            for(int i=0;i<5;i++)
                alternativas.get(i).setValue(((QuestaoObjetiva)questao).getAlternativas().get(i));
        }
        if(prova.getTipo().equals(TipoProva.DISCUSSIVA)){
            alternativas.get(0).setLabel("Resposta Esperada");
            alternativas.get(0).setValue(
                    ((QuestaoDiscursiva)questao).getRespostaEsperada()
            );
            for(int i=1;i<5;i++)
                alternativas.get(i).setVisible(false);
        }
    }

}
