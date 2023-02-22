package br.ufg.sep.views.revisao.presenter;

import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.entity.*;
import br.ufg.sep.views.revisao.RevisaoLinguagemQuestaoView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.List;

public class RevisaoLinguagemQuestaoPresenter {
        RevisaoLinguagemQuestaoView view;
        QuestaoRepository questaoRepository;

        Questao questao;

        Prova prova;


    public RevisaoLinguagemQuestaoPresenter(Questao questao,RevisaoLinguagemQuestaoView view, QuestaoRepository questaoRepository){
    this.view = view;
    this.questaoRepository = questaoRepository;
    this.questao = questao;
    this.prova = questao.getProva();
    setQuestaoInfo();
    configBotoes();
    }

    private void configBotoes(){
        view.getEnviarBanca().addClickListener(c->{
        this.questao.enviarParaBanca(questao.getState().getRevisao());
        questaoRepository.save(questao);
        });
    }


    private void setQuestaoInfo() throws NullPointerException{
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
