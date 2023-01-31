package br.ufg.sep.views.concurso;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
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

import br.sep.presenter.FormularioConcursoPresenter;
import br.ufg.sep.views.MainLayout;

@Route(value="cadastrar_concursos", layout = MainLayout.class)
@PageTitle("Concursos")
@RolesAllowed({"ADMIN", "PED"})
public class FormularioConcursoView extends VerticalLayout {
	
	Button save;
	Button cancel;
	TextField nomeTF;
	TextField cidadeTF;
	DatePicker dataInicioDP;
	DatePicker dataFimDP;
	VerticalLayout layout;
	HorizontalLayout buttonLayout;
	//FormularioConcursoPresenter formPresenter;
	
	public FormularioConcursoView() {
		
		criarTela();
		//formPresenter = new FormularioConcursoPresenter(this);
		
		save.addClickListener(new FormularioConcursoPresenter(this));
		cancel.addClickListener(new FormularioConcursoPresenter(this));
        
        setPadding(true);
        add(layout, buttonLayout);
        
	}	
	
	private void criarTela() {

		DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
		singleFormatI18n.setDateFormat("dd/MM/yyyy");
		
		nomeTF = new TextField("Nome", "", "");
        cidadeTF = new TextField("Cidade", "", "");
        dataInicioDP = new DatePicker("Data de Início");
        dataFimDP = new DatePicker("Data Fim");
        
        dataInicioDP.setI18n(singleFormatI18n);
        dataFimDP.setI18n(singleFormatI18n);
        dataFimDP.setPlaceholder("DD/MM/AAAA");
        dataInicioDP.setPlaceholder("DD/MM/AAAA");
        
        FormLayout formLayout = new FormLayout(nomeTF, cidadeTF);
        FormLayout formData = new FormLayout(dataInicioDP, dataFimDP);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formData.setResponsiveSteps(new ResponsiveStep("0", 2));
        //formLayout.setMaxWidth("700px");
        //formData.setMaxWidth("700px");

        save = new Button("Salvar");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel = new Button("Cancel");
        
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

	public TextField getNomeTF() {
		return nomeTF;
	}

	public TextField getCidadeTF() {
		return cidadeTF;
	}

	public DatePicker getDataInicioDP() {
		return dataInicioDP;
	}

	public DatePicker getDataFimDP() {
		return dataFimDP;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public void setButtonLayout(HorizontalLayout buttonLayout) {
		this.buttonLayout = buttonLayout;
	}
	
	
}
