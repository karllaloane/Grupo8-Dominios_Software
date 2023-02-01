package br.ufg.sep.views.concurso.presenter;

import java.time.LocalDate;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

import br.ufg.sep.data.repositories.ConcursoRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.concurso.ConcursosView;
import br.ufg.sep.views.concurso.EditarConcursoView;
import br.ufg.sep.views.concurso.FormularioConcursoView;

public class EditarConcursoPresenter  {

	Concurso concurso;
	ConcursoService concursoService;
	
	public EditarConcursoPresenter(EditarConcursoView editarConcursoView, ConcursoService cS) {
		//concursoView = formularioConcursoViewImpl;
		this.concursoService = cS;
		
		
		editarConcursoView.getCancel().addClickListener(e->{
			 editarConcursoView.getCancel().getUI().ifPresent(ui->{
			 ui.navigate(ConcursosView.class); });
		});
		
		editarConcursoView.getSave().addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				// TODO Auto-generated method stub
				salvarConcurso(event, editarConcursoView);				
			}
		});
	}
	
	private void salvarConcurso(ClickEvent<Button> event, EditarConcursoView concursoView) {
		String nomeConcurso = concursoView.getNome().getValue();	
		String nomeCidade = concursoView.getCidade().getValue();
		LocalDate dataInicio;
		LocalDate dataFim;
		
		//verificando campos em branco
		if(nomeCidade.isEmpty() || nomeConcurso.isEmpty() || concursoView.getDataInicio().isEmpty()
				|| concursoView.getDataFim().isEmpty()) {
			
			Notification notification = Notification
			        .show("Campos em branco!");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return;
		}
		
		dataInicio = concursoView.getDataInicio().getValue();
		dataFim = concursoView.getDataFim().getValue();
			
		//validando a data
		if(dataInicio.isAfter(dataFim)) {
			Notification notification = Notification
			        .show("A data de início não pode ser posterior a data final!");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return;
		}
			
		Concurso concurso = new Concurso();
		concurso.setNome(nomeConcurso);
		concurso.setCidade(nomeCidade);
		concurso.setDataInicio(dataInicio);
		concurso.setDataFim(dataFim);
		concurso.setId(concursoView.getConcurso().getId());
			
		concursoService.save(concurso);		
		
		Notification notification = Notification
		        .show("Alterações salvas com sucesso!");
		notification.setPosition(Position.TOP_CENTER);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		event.getSource().getUI().ifPresent(ui -> ui.navigate(ConcursosView.class));						
	}

}