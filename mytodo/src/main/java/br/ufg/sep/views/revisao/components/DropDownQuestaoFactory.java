package br.ufg.sep.views.revisao.components;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.*;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class DropDownQuestaoFactory {
    Questao questao;
    Prova prova;
    private TextArea subAreasQuestao = new TextArea("Sub-áreas da questão ", "", "");
    private TextArea enunciadoQuestao = new TextArea("Enunciado", "", "");
    private TextField nivelDificuldadeQuestaoCombo = new TextField("Nível de Dificuldade", "", "");
    private TextArea justificativaQuestao = new TextArea("Justificativa da Alternativa Correta", "", "");
    private TextArea alternativaAQuestao = new TextArea("A) ", "", "");
    private TextArea alternativaBQuestao = new TextArea("B) ", "", "");
    private TextArea alternativaCQuestao = new TextArea("C) ", "", "");
    private TextArea alternativaDQuestao = new TextArea("D) ", "", "");
    private TextArea alternativaEQuestao = new TextArea("E) ", "", "");
    private TextArea orientacoesQuestao = new TextArea("Orientações da Revisão", "", "");
    private TextField estadoQuestao = new TextField("Estado da questão");

    private Details details3;

    public Text tituloSumario = new Text("Questão - Informações Gerais");
    public DropDownQuestaoFactory(Questao questao){
        this.questao = questao;
        this.prova = questao.getProva();
        HorizontalLayout summary = new HorizontalLayout();
        summary.setSpacing(false);
        summary.add(tituloSumario);

        /* Formatando os campos */
        enunciadoQuestao.setWidth("700px");
        nivelDificuldadeQuestaoCombo.setWidth("310px");
        justificativaQuestao.setWidth("1025px");
        alternativaAQuestao.setWidth("700px");
        alternativaBQuestao.setWidth("700px");
        alternativaCQuestao.setWidth("700px");
        alternativaDQuestao.setWidth("700px");
        alternativaEQuestao.setWidth("700px");
        subAreasQuestao.setWidth("512px");

        /* Deixando campos não editaveis  */
        enunciadoQuestao.setReadOnly(true);
        nivelDificuldadeQuestaoCombo.setReadOnly(true);
        justificativaQuestao.setReadOnly(true);
        alternativaAQuestao.setReadOnly(true);
        alternativaBQuestao.setReadOnly(true);
        alternativaCQuestao.setReadOnly(true);
        alternativaDQuestao.setReadOnly(true);
        alternativaEQuestao.setReadOnly(true);
        subAreasQuestao.setReadOnly(true);
        estadoQuestao.setReadOnly(true);
        /* Layout final de questão */
        HorizontalLayout horizontalLayout1 = new HorizontalLayout(enunciadoQuestao,
                new VerticalLayout(nivelDificuldadeQuestaoCombo,subAreasQuestao,estadoQuestao));
        HorizontalLayout horizontalLayout2 = new HorizontalLayout(justificativaQuestao);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        VerticalLayout infosForm = new VerticalLayout(horizontalLayout1, horizontalLayout3, alternativaAQuestao, alternativaBQuestao,
                alternativaCQuestao, alternativaDQuestao, alternativaEQuestao, horizontalLayout2);

        /* Drop menu*/
        details3 = new Details(summary, infosForm);
        details3.addThemeVariants(DetailsVariant.FILLED);
        details3.setWidthFull();
        details3.setMinWidth("1070px");
        details3.setOpened(false);
        configFields();
    }

    public Details getComponent(){
        return this.details3;
    }

    private void configFields() {
        String subAreas = new String("");
        for(String sub : questao.getSubAreas()){
            subAreas+=sub+"\n";
        }

        this.getSubAreasQuestao().setValue(subAreas);
        this.getEnunciadoQuestao().setValue(questao.getEnunciado());
        this.getNivelDificuldadeQuestaoCombo().setValue(questao.getNivelDificuldade().toString());
        if(questao.getState()!=null)
        this.getEstadoQuestao().setValue(questao.getState().toString());
        else this.getEstadoQuestao().setVisible(false);
        List<TextArea> alternativas = List.of(
                this.getAlternativaAQuestao(),
                this.getAlternativaBQuestao(),
                this.getAlternativaCQuestao(),
                this.getAlternativaDQuestao(),
                this.getAlternativaEQuestao()
        );
        if(prova.getTipo().equals(TipoProva.OBJETIVA_4)){
            this.getJustificativaQuestao().setValue(questao.getJustificativa());
            for(int i=0;i<4;i++)
                alternativas.get(i).setValue(((QuestaoObjetiva)questao).getAlternativas().get(i));
            alternativas.get(4).setVisible(false);
        }
        if(prova.getTipo().equals(TipoProva.OBJETIVA_5)){
            this.getJustificativaQuestao().setValue(questao.getJustificativa());
            for(int i=0;i<5;i++)
                alternativas.get(i).setValue(((QuestaoObjetiva)questao).getAlternativas().get(i));
        }
        if(prova.getTipo().equals(TipoProva.DISCUSSIVA)){
            this.getJustificativaQuestao().setVisible(false);
            alternativas.get(0).setLabel("Resposta Esperada");
            alternativas.get(0).setValue(
                    ((QuestaoDiscursiva)questao).getRespostaEsperada()
            );
            for(int i=1;i<5;i++)
                alternativas.get(i).setVisible(false);
        }


    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }

    public TextArea getSubAreasQuestao() {
        return subAreasQuestao;
    }

    public void setSubAreasQuestao(TextArea subAreasQuestao) {
        this.subAreasQuestao = subAreasQuestao;
    }

    public TextArea getEnunciadoQuestao() {
        return enunciadoQuestao;
    }

    public void setEnunciadoQuestao(TextArea enunciadoQuestao) {
        this.enunciadoQuestao = enunciadoQuestao;
    }

    public TextField getNivelDificuldadeQuestaoCombo() {
        return nivelDificuldadeQuestaoCombo;
    }

    public void setNivelDificuldadeQuestaoCombo(TextField nivelDificuldadeQuestaoCombo) {
        this.nivelDificuldadeQuestaoCombo = nivelDificuldadeQuestaoCombo;
    }

    public TextArea getJustificativaQuestao() {
        return justificativaQuestao;
    }

    public void setJustificativaQuestao(TextArea justificativaQuestao) {
        this.justificativaQuestao = justificativaQuestao;
    }

    public TextArea getAlternativaAQuestao() {
        return alternativaAQuestao;
    }

    public void setAlternativaAQuestao(TextArea alternativaAQuestao) {
        this.alternativaAQuestao = alternativaAQuestao;
    }

    public TextArea getAlternativaBQuestao() {
        return alternativaBQuestao;
    }

    public void setAlternativaBQuestao(TextArea alternativaBQuestao) {
        this.alternativaBQuestao = alternativaBQuestao;
    }

    public TextArea getAlternativaCQuestao() {
        return alternativaCQuestao;
    }

    public void setAlternativaCQuestao(TextArea alternativaCQuestao) {
        this.alternativaCQuestao = alternativaCQuestao;
    }

    public TextArea getAlternativaDQuestao() {
        return alternativaDQuestao;
    }

    public void setAlternativaDQuestao(TextArea alternativaDQuestao) {
        this.alternativaDQuestao = alternativaDQuestao;
    }

    public TextArea getAlternativaEQuestao() {
        return alternativaEQuestao;
    }

    public void setAlternativaEQuestao(TextArea alternativaEQuestao) {
        this.alternativaEQuestao = alternativaEQuestao;
    }

    public TextArea getOrientacoesQuestao() {
        return orientacoesQuestao;
    }

    public void setOrientacoesQuestao(TextArea orientacoesQuestao) {
        this.orientacoesQuestao = orientacoesQuestao;
    }

    public TextField getEstadoQuestao() {
        return estadoQuestao;
    }

    public void setEstadoQuestao(TextField estadoQuestao) {
        this.estadoQuestao = estadoQuestao;
    }

    public Details getDetails3() {
        return details3;
    }

    public void setDetails3(Details details3) {
        this.details3 = details3;
    }
}
