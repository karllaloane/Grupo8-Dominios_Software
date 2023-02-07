package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.data.repositories.ConcursoRepository;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.concurso.ConcursosView;
import br.ufg.sep.views.concurso.FormularioConcursoView;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
import org.springframework.orm.jpa.JpaSystemException;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import java.time.LocalDate;
import java.util.List;

public class NovaProvaPresenter {
	
    NovaProvaView view;
    Concurso concurso;
    Cadastro cadastro;
    ProvaService provaService; 

    public NovaProvaPresenter(NovaProvaView view, ProvaService provaService){
       this.provaService = provaService; 
       this.view = view;
       
       /* Pega o cadastro selecionado na Grid*/
       view.getColaboradoresGrid().addItemClickListener(item->{
           cadastro = item.getItem();

           view.getColaboradorAssociado().setValue(cadastro.getNome());
       });
       
       view.getSalvarButton().addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				
		        
				salvarProva(event, view, cadastro);				
			}
		});

    }
    
    private void salvarProva(ClickEvent<Button> event, NovaProvaView novaProvaView, Cadastro cadastro) {
    	this.cadastro = cadastro;  
    	
    	/* Criando e armazenando os valores do Imput*/
    	Concurso concurso = novaProvaView.getConcurso();	
		String areaconhecimento = novaProvaView.getAreaConhecimento().getValue();
		String descricao = novaProvaView.getDescricaoDaProva().getValue();
		int numQuestoes = 0; 
		numQuestoes = Integer.parseInt(novaProvaView.getNumQuestoes().getValue());
		LocalDate prazo = novaProvaView.getPrazo().getValue();
		
		
		
		//verificando campos em branco
		if(areaconhecimento.isEmpty() || novaProvaView.getPrazo().isEmpty() 
				|| descricao.isEmpty() || numQuestoes == 0) {
					
				/* Notifica que existe campo em branco*/
				Notification notification = Notification
					    .show("Campos em branco!");
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
				return;
		}
		
		/*Instancia uma Prova*/
		Prova prova = new Prova(); 
		
		/*Instancia os atributos de prova*/
		prova.setConcurso(concurso);
		prova.setAreaConhecimento(areaconhecimento);
		prova.setNumeroQuestoes(numQuestoes);
		// prova.setPrazo(prazo); - Criar atributo prazo em prova
		prova.setDescricao(descricao);
		prova.setResponsavel(cadastro);
		
		/*Salva prova no Banco de Dados*/

		
		/*Notifica ação bem sucedida*/
		Notification notification = Notification
		        .show("Prova salva com sucesso!");
		notification.setPosition(Position.TOP_CENTER);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		
		/*Volta para a tela de NovaProvaView*/
		event.getSource().getUI().ifPresent(ui -> ui.navigate(NovaProvaView.class));
    }
}
