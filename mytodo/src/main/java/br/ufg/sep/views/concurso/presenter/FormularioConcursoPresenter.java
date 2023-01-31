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
import br.ufg.sep.views.concurso.FormularioConcursoView;

public class FormularioConcursoPresenter implements ComponentEventListener<ClickEvent<Button>> {

	Concurso concurso;
	FormularioConcursoView cv;
	ConcursoService concursoService;
	
	public FormularioConcursoPresenter(FormularioConcursoView formularioConcursoViewImpl, ConcursoService cS) {
		cv = formularioConcursoViewImpl;
		this.concursoService = cS;
	}
	
	private boolean salvarConcurso() {
		
		return false;		
	}

	@Override
	public void onComponentEvent(ClickEvent<Button> event) {
		// TODO Auto-generated method stub
		
		//botao salvar
		if(event.getSource().getText().equals("Salvar")) {
			botaoSalvar(event);
		}
		//botao cancelar
		else {
			event.getSource().getUI().ifPresent(ui -> ui.navigate(ConcursosView.class));
		}
	}

	private void botaoSalvar(ClickEvent<Button> event) {
		String nomeConcurso = cv.getNomeTF().getValue();	
		String nomeCidade = cv.getCidadeTF().getValue();
		LocalDate dataInicio;
		LocalDate dataFim;
		
		//verificando campos em branco
		if(nomeCidade.isEmpty() || nomeConcurso.isEmpty() || cv.getDataInicioDP().isEmpty()
				|| cv.getDataFimDP().isEmpty()) {
			
			Notification notification = Notification
			        .show("Campos em branco!");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			
		} else {
			dataInicio = cv.getDataInicioDP().getValue();
			dataFim = cv.getDataFimDP().getValue();
			
			//validando a data
			if(dataInicio.isAfter(dataFim)) {
				Notification notification = Notification
				        .show("A data de início não pode ser posterior a data final!");
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			} else {
				
				Concurso concurso = new Concurso();
				concurso.setNome(nomeConcurso);
				concurso.setCidade(nomeCidade);
				concurso.setDataInicio(dataInicio);
				concurso.setDataFim(dataFim);
				
				
				concursoService.save(concurso);		
				
				Notification notification = Notification
				        .show("Concurso salvo com sucesso!");
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				event.getSource().getUI().ifPresent(ui -> ui.navigate(ConcursosView.class));	
			}				
		}		
	}

}
