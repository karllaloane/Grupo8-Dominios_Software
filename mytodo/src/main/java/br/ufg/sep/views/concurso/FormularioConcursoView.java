package br.ufg.sep.views.concurso;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Responsive;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.views.MainLayout;

@Route(value="cadastrar_concursos", layout = MainLayout.class)
@PageTitle("Concursos")
@RolesAllowed({"ADMIN", "PED"})
public class FormularioConcursoView extends VerticalLayout{
	
	public FormularioConcursoView() {
		
		DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
		singleFormatI18n.setDateFormat("dd/MM/yyyy");
		
		TextField nomeTF = new TextField("Nome", "", "");
        TextField cidadeTF = new TextField("Cidade", "", "");
        DatePicker dataInicioDP = new DatePicker("Data de Início");
        DatePicker dataFimDP = new DatePicker("Data Fim");
        
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

        Button save = new Button("Salvar");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("Cancel");
        
        VerticalLayout layout = new VerticalLayout(formLayout,
        		formData);
        //layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMaxWidth("700px");

        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel);
        buttonLayout.setPadding(true);
        
        setPadding(true);
        add(layout, buttonLayout);
	}
}
