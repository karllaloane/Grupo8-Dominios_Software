package br.ufg.sep.views.gerenciarProvas;

import javax.annotation.security.RolesAllowed;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.gerenciarProvas.presenter.NovaProvaPresenter;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.views.MainLayout;

import java.util.Optional;


@Route(value="visualizar-prova", layout=MainLayout.class)
@PageTitle("Visualizar Provas")
@RolesAllowed({"ADMIN","PED"})
public class VisualizarProvaView extends EditarProvaView {
	public VisualizarProvaView(ProvaService provaService, ConcursoService concursoService,
							   CadastroRepository cadastroRepository){
		super(provaService,concursoService,cadastroRepository);
		setEverythingReadOnly(this); //função recursiva a qual seta tudo na view para readOnly
		this.salvarButton.setVisible(false);
	}

	private void setEverythingReadOnly(Component component) {
		if(component==null) return;

		// o método setReadOnly só existe nas classes que herdam de Abstract Field
		if(component instanceof AbstractField){
			((AbstractField)component).setReadOnly(true);
			return;
		}
		//para cada filho de algum componente, procura por heranças de Abstract Field recursivamente.
		component.getChildren().forEach(ch->{
			this.setEverythingReadOnly(ch);
		});

	}



}
