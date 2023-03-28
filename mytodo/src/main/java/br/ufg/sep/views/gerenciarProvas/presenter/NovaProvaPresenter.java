package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.entity.*;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.views.gerenciarProvas.EditarProvaView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.data.domain.PageRequest;

import javax.naming.directory.InvalidAttributeIdentifierException;
import java.time.LocalDate;

public class NovaProvaPresenter {
	
    NovaProvaView view;

    public NovaProvaPresenter(NovaProvaView view, ProvaService provaService){
       this.view = view;
		configComboBox();
		view.getSalvarButton().addClickListener( e->salvarProva(e));
		view.getRadioTipoProva().addValueChangeListener(value->{
			if(value.getValue().equals("Objetiva")){
				view.getRadioNivelNumAlternativas().setVisible(true);
			}else{
				view.getRadioNivelNumAlternativas().setVisible(false);
			}
		});
    }

	public TipoProva decidirTipo(String tipoSelecionado)throws InvalidAttributeIdentifierException{

		if(tipoSelecionado.toLowerCase().contains("objetiva")) {
			if(view.getRadioNivelNumAlternativas().getValue().equals("4"))
				return TipoProva.OBJETIVA_4;
			else 
				return TipoProva.OBJETIVA_5;		
				
		}

		if(tipoSelecionado.toLowerCase().contains("discursiva"))
			return TipoProva.DISCUSSIVA;
		
		if(
				tipoSelecionado.toLowerCase().contains("redação")
				|| tipoSelecionado.toLowerCase().contains("redacao")
				|| tipoSelecionado.toLowerCase().contains("redacão")
		)
			return TipoProva.REDACAO;

		throw new InvalidAttributeIdentifierException();
	}

	NivelProva decidirNivel(String nivelSelecionado)throws InvalidAttributeIdentifierException {

		if(nivelSelecionado.toLowerCase().contains("fundamental"))
			return NivelProva.FUNDAMENTAL;
		if(nivelSelecionado.toLowerCase().contains("médio") ||nivelSelecionado.toLowerCase().contains("medio") )
			return NivelProva.MEDIO;
		if(nivelSelecionado.toLowerCase().contains("superior"))
			return NivelProva.SUPERIOR;
		throw new InvalidAttributeIdentifierException();
	}
    private void salvarProva(ClickEvent<Button> event) {

    	/* Criando e armazenando os valores do Input*/
    	Concurso concurso = view.getConcurso();
		String areaconhecimento = view.getAreaConhecimento().getValue();
		String descricao = view.getDescricaoDaProva().getValue();
		int numQuestoes = 0; 
		numQuestoes = Integer.parseInt(view.getNumQuestoes().getValue());
		LocalDate prazo = view.getPrazo().getValue();
		TipoProva tipo=null; // Inicializando apenas para compilar
		NivelProva nivel=null ;// Inicializando apenas para compilar
		/*************/
		//Atribuindo a escolha do TIPO da prova:
		try {
			tipo = decidirTipo(view.getRadioTipoProva().getValue());
		}catch (Exception exp) {
			System.out.println(exp.getMessage());}

		//Atribuindo a escolha do NIVEL da prova:
		try{
			nivel = decidirNivel(view.getRadioNivelProva().getValue());
		}catch (Exception exp){System.out.println(exp.getMessage());}
		/**************/
		
		//verificando campos em branco
		if(areaconhecimento.isEmpty() || view.getPrazo().isEmpty()
				|| descricao.isEmpty() || numQuestoes == 0
		|| tipo==null || nivel==null) {
					
				/* Notifica que existe campo em branco*/
				Notification notification = Notification
					    .show("Campos em branco!");
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
				return;
		}




		
		/*Instancia uma Prova*/
		Prova prova;
		if(view instanceof  EditarProvaView )
			prova = view.getProvaService().getRepository().findById(view.getParameterId()).get();
		else
			prova = new Prova();


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
		//Revisor Técnico III 
		prova.setRevisor3(
				view.getComboBoxMembroRevisorTecnico2().getValue()
				
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
		prova.setNivel(nivel);
		prova.setTipo(tipo);

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
		if(this.view instanceof EditarProvaView)
		view.getSalvarButton().getUI().ifPresent(ui -> {
			ui.navigate(GerenciarProvasView.class,prova.getConcurso().getId());
		});
		view.getSalvarButton().getUI().ifPresent(ui -> {
			ui.navigate(GerenciarProvasView.class,view.getParameterId());
		});


    }

	private void configComboBox(){
		// para cara comboBox da grid adiconado em
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
