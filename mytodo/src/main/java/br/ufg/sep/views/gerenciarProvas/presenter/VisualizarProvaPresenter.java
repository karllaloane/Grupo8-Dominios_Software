package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.entity.*;
import br.ufg.sep.views.concurso.ConcursosView;
import br.ufg.sep.views.concurso.EditarConcursoView;
import br.ufg.sep.views.gerenciarProvas.EditarProvasView;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.naming.directory.InvalidAttributeIdentifierException;
import java.time.LocalDate;

public class VisualizarProvaPresenter {
	
    NovaProvaView view;
    Prova prova; 
    ProvaService provaService; 

    public VisualizarProvaPresenter(EditarProvasView editarProvasView, ProvaService ps){
    	this.provaService = ps; 
    	
    
    	
    	
    }
    
       

}
