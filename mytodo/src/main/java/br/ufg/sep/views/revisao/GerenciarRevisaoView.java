package br.ufg.sep.views.revisao;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.entity.NivelDificuldade;
import br.ufg.sep.views.MainLayout;

@Route(value="gerenciar-revisao", layout = MainLayout.class)
@PageTitle("Questões")
@PermitAll

public class GerenciarRevisaoView extends HorizontalLayout{
	
	/* Imputs do concurso */ 
	private TextField nomeConcurso = new TextField("Nome", "", "");
	private TextField cidadeConcurso = new TextField("Cidade", "", "");
	private DatePicker dataInicioConcurso = new DatePicker();
	private DatePicker dataFimConcurso = new DatePicker();
	
	/* Imputs da prova */
	private TextField areaConhecimento = new TextField("Área de conhecimento", "", "");
    private TextField numQuestoes = new TextField("Número de questões", "", "");
    private TextField tipoProva = new TextField("Tipo de Prova", "", "");
    private TextField numAlternativas = new TextField("Número de Alternativas ", "", "");
    private TextField nivelProva = new TextField("Nível da Prova", "", "");
    private TextField revisorTecnico1 = new TextField("Revisor técnico 1", "", "");
    private TextField revisorTecnico2 = new TextField("Revisor técnico 2", "", "");
    private TextField revisorTecnico3 = new TextField("Revisor técnico 3", "", "");
    private TextField revisorLinguagem = new TextField("Revisor de linguagem", "", "");
    private TextField membroBanca = new TextField("Membro da Banca de questões", "", "");
    private DatePicker prazo;
    private TextArea descricaoDaProva = new TextArea("Descriçãop da Prova", "", "");
    private Button baixarAnexo = new Button("Baixar Anexo"); 
    
    /* Imputs da questão */
    private TextArea enunciadoQuestao = new TextArea("Enunciado", "", "");
	private List<TextArea> alternativasQuestaoList;
	private List<Checkbox> checkboxQuestaoList;
	private TextField subareaQuestao = new TextField("Sub-Área", "", "");
	private TextField nivelDificuldadeQuestaoCombo = new TextField("Nível de Dificuldade", "", "");
	private TextArea justificativaQuestao;
	
	
	

}
