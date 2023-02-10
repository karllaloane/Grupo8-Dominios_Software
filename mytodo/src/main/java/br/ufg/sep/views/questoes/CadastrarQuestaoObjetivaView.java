package br.ufg.sep.views.questoes;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.questoes.presenter.VisualizarQuestoesProvaPresenter;

@Route(value="cadastrar_questoes_prova", layout = MainLayout.class)
@PageTitle("Cadastrar Questão")
@PermitAll
public class CadastrarQuestaoObjetivaView extends VerticalLayout implements HasUrlParameter<Long>{

	private ProvaService provaService;
	private QuestaoService questaoService;

	private Prova prova;
	//private Long provaId;
	
	public CadastrarQuestaoObjetivaView(ProvaService provaService, QuestaoService questaoService) {
		this.provaService = provaService;
		this.questaoService = questaoService;
		
		
	}


	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		// TODO Auto-generated method stub
		
		Optional<Prova> optionalQuestao = provaService.getRepository().findById(parameter);
		if (optionalQuestao.isPresent()) {
			prova = optionalQuestao.get();
			//this.provaId = prova.getId();

			//this.presenter = new VisualizarQuestoesProvaPresenter(provaService, questaoService,this); //iniciar o presenter
		}
		
	}
	
}
