package br.ufg.sep.views.gerencia;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerencia.presenter.EditarConcursoPresenter;

@Route(value="editar_concurso", layout = MainLayout.class)
@PageTitle("Concursos")
@RolesAllowed({"ADMIN", "PED"})
public class EditarConcursoView extends VerticalLayout implements HasUrlParameter<Long> {
	
	Button save;
	Button cancel;
	TextField nome;
	TextField cidade;
	DatePicker dataInicio;
	DatePicker dataFim;
	VerticalLayout layout;
	HorizontalLayout buttonLayout;
	Concurso concurso;
	ConcursoService service;
	//FormularioConcursoPresenter formPresenter;
	
	public EditarConcursoView(ConcursoService cS) {
		
		this.service = cS;
		
		criarTela();
		
		EditarConcursoPresenter formPresenter = new EditarConcursoPresenter(this, cS);
        
        setPadding(true);
        add(layout, buttonLayout);
        
	}	
	
	private void criarTela() {

		DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
		singleFormatI18n.setDateFormat("dd/MM/yyyy");
		
		nome = new TextField("Nome", "", "");
        cidade = new TextField("Cidade", "", "");
        dataInicio = new DatePicker("Data de Início");
        dataFim = new DatePicker("Data Fim");
        
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

        save = new Button("Salvar alterações");
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
	
	public Button getCancel() {
		return cancel;
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		Optional<Concurso> optionalConcurso = service.getRepository().findById(parameter);
		if (optionalConcurso.isPresent()) {
			concurso = optionalConcurso.get();
			
			this.nome.setValue(concurso.getNome());
			this.cidade.setValue(concurso.getCidade());
			this.dataFim.setValue(concurso.getDataFim());
			this.dataInicio.setValue(concurso.getDataInicio());
			
		} else {
			Notification notification = Notification
			        .show("Impossível acessar o concurso");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}
		
	}
	
	public Concurso getConcurso() {
		return concurso;
	}
}
