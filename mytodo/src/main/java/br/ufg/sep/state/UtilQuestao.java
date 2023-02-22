package br.ufg.sep.state;

import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.QuestaoDiscursiva;
import br.ufg.sep.entity.QuestaoObjetiva;
import br.ufg.sep.entity.TipoProva;
import jdk.jshell.execution.Util;
import org.hibernate.query.criteria.internal.OrderImpl;

public class UtilQuestao {

    public static void copiarQuestao(Questao origem, Questao desino){
        if(origem instanceof  QuestaoObjetiva){
            UtilQuestao.copiarQuestao((QuestaoObjetiva)origem,(QuestaoObjetiva)desino);
        }else if(origem instanceof  QuestaoDiscursiva){
            UtilQuestao.copiarQuestao((QuestaoDiscursiva) origem,(QuestaoDiscursiva) desino);
        }

    }
    private static void copiarQuestao(QuestaoDiscursiva origem, QuestaoDiscursiva destino) {


        QuestaoDiscursiva questaoDiscursiva = origem;
        destino.setNivelDificuldade(questaoDiscursiva.getNivelDificuldade());
        destino.setJustificativa(questaoDiscursiva.getJustificativa());
        destino.setEnunciado(questaoDiscursiva.getEnunciado());
        destino.setProva(questaoDiscursiva.getProva());
        destino.setRespostaEsperada(questaoDiscursiva.getRespostaEsperada());
        destino.setState(null);
    }

    private static void copiarQuestao(QuestaoObjetiva origem, QuestaoObjetiva destino) {

        if (origem.getProva().getTipo().equals(TipoProva.OBJETIVA_4)
                || origem.getProva().getTipo().equals(TipoProva.OBJETIVA_5)
        ) {
            QuestaoObjetiva questaoObjetiva = (QuestaoObjetiva) origem;
            destino.setNivelDificuldade(questaoObjetiva.getNivelDificuldade());
            destino.setJustificativa(questaoObjetiva.getJustificativa());
            destino.setEnunciado(questaoObjetiva.getEnunciado());
            destino.setProva(questaoObjetiva.getProva());
            destino.setAlternativas(questaoObjetiva.getAlternativas());
            destino.setAlternativaCorreta(questaoObjetiva.getAlternativaCorreta());
            destino.setQuantAlternativas(questaoObjetiva.getQuantAlternativas());
            destino.setState(null);

        }

    }

}
