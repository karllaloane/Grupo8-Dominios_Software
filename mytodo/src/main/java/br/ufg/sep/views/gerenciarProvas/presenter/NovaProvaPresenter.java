package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public class NovaProvaPresenter {
	
    NovaProvaView view;

    public NovaProvaPresenter(NovaProvaView view, ProvaService provaService){
       this.view = view;
		configComboBox();
		view.getSalvarButton().addClickListener( e->salvarProva(e));
    }
  
    private void salvarProva(ClickEvent<Button> event) {

    	/* Criando e armazenando os valores do Input*/
    	Concurso concurso = view.getConcurso();
		String areaconhecimento = view.getAreaConhecimento().getValue();
		String descricao = view.getDescricaoDaProva().getValue();
		int numQuestoes = 0; 
		numQuestoes = Integer.parseInt(view.getNumQuestoes().getValue());
		LocalDate prazo = view.getPrazo().getValue();

		
		
		//verificando campos em branco
		if(areaconhecimento.isEmpty() || view.getPrazo().isEmpty()
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

		/*Setar as relações da prova*/
		prova.setConcurso(concurso);

		//Elaborador
		prova.setElaborador(
				view.getComboBoxMembroBancaQuestao()
						.getValue()
		);
		//Revisor Técnico I
		prova.setRevisor1(
				view.getComboBoxMembroRevisorTecnico1().getValue()
		);
		//Revisor Técnico II
		prova.setRevisor2(
				view.getComboBoxMembroRevisorTecnico2().getValue()
		);
		//Revisor Técnico III *****************************TROCAR***************************************88888
		prova.setRevisor3(
				view.getComboBoxMembroRevisorTecnico2().getValue()
				//O INPUT DO 2 TA INDO NO 3 APENAS PARA TESTEEEEEEEEEEEEEEEEEEEEEEEEE
		);
		//Revisor Linguistico
		prova.setRevisorLinguagem(
				view.getComboBoxMembroRevisorLinguagem().getValue()
		);

		/*Seta os atributos de prova*/
		prova.setAreaConhecimento(areaconhecimento);
		prova.setNumeroQuestoes(numQuestoes);
		prova.setDataEntrega(prazo);
		prova.setDescricao(descricao);


		/*Adiciona a prova no concurso*/
		concurso.getProvas().add(prova);

		/*Salva prova no Banco de Dados por meio do CONCURSO*/
		this.view.getConcursoService().save(concurso);
		
		/*Notifica ação bem sucedida*/
		Notification notification = Notification
		        .show("Prova salva com sucesso!");
		notification.setPosition(Position.TOP_CENTER);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		
		/*Volta para a tela de NovaProvaView*/
		view.getSalvarButton().getUI().ifPresent(ui -> {
			ui.navigate(GerenciarProvasView.class,view.getConcursoId());
		});
    }


	private void configComboBox(){// para cara comboBox da grid adiconado em
		//   utilArrayComboBoxCadastro, ele irá fazer a mesma coisa
		view.getUtilArrayComboBoxCadastro().forEach(comboBox->{
			comboBox.setItems(
					query-> view.getCadastroRepository()
							.findAll(PageRequest.of(
									query.getPage(),
									query.getPageSize()
							)).stream()
			);
		});
	}


}
