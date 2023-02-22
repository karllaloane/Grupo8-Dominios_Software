package br.ufg.sep.views.gerencia;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerencia.presenter.FormularioConcursoPresenter;

@Route(value="cadastrar_concursos", layout = MainLayout.class)
@PageTitle("Concursos")
@RolesAllowed({"ADMIN", "PED"})
public class FormularioConcursoView extends VerticalLayout {
	
	Button save;
	Button cancel;
	TextField nome;
	TextField cidade;
	DatePicker dataInicio;
	DatePicker dataFim;
	VerticalLayout layout;
	HorizontalLayout buttonLayout;
	//FormularioConcursoPresenter formPresenter;
	
	public FormularioConcursoView(ConcursoService cS) {
		
		/* Método descrito abaixo, cria uma tela já com alguns campos editaveis*/ 
		criarTela();
		
		FormularioConcursoPresenter formPresenter = new FormularioConcursoPresenter(this, cS);
        
        setPadding(true);
        add(layout, buttonLayout);
        
	}	
	
	private void criarTela() {

		/* Formatando o atributo prazo do tipo DatePicker para dd/MM/yyyy*/ 
		DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
		singleFormatI18n.setDateFormat("dd/MM/yyyy");
		
		/*Inicializando os atributos*/
		nome = new TextField("Nome", "", "");
        cidade = new TextField("Cidade", "", "");
        dataInicio = new DatePicker("Data de Início");
        dataFim = new DatePicker("Data Fim");
        
        /*Formatando datas*/
        dataInicio.setI18n(singleFormatI18n);
        dataFim.setI18n(singleFormatI18n);
        dataFim.setPlaceholder("DD/MM/AAAA");
        dataInicio.setPlaceholder("DD/MM/AAAA");
        
        //setando para receber apenas número e /
        dataInicio.setAllowedCharPattern(("[0-9/]"));
        dataFim.setAllowedCharPattern(("[0-9/]"));
        
        FormLayout formLayout = new FormLayout(nome, cidade);
        FormLayout formData = new FormLayout(dataInicio, dataFim);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formData.setResponsiveSteps(new ResponsiveStep("0", 2));
        //formLayout.setMaxWidth("700px");
        //formData.setMaxWidth("700px");

        save = new Button("Salvar");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel = new Button("Cancelar");
        
        layout = new VerticalLayout(formLayout, formData);
        //layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMaxWidth("700px");

        buttonLayout = new HorizontalLayout(save, cancel);
        buttonLayout.setPadding(true);
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public TextField getNome() {
		return nome;
	}

	public TextField getCidade() {
		return cidade;
	}

	public DatePicker getDataInicio() {
		return dataInicio;
	}

	public DatePicker getDataFim() {
		return dataFim;
	}

	public void setButtonLayout(HorizontalLayout buttonLayout) {
		this.buttonLayout = buttonLayout;
	}
	
	public Button getCancel() {
		return cancel;
	}
}
