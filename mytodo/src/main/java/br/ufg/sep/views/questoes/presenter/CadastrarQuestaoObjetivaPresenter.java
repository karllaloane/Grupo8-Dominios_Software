package br.ufg.sep.views.questoes.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufg.sep.entity.*;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;

import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.views.questoes.CadastrarQuestaoObjetivaView;

public class CadastrarQuestaoObjetivaPresenter {

	private ProvaService provaService;
	private QuestaoService questaoService;
	private CadastrarQuestaoObjetivaView view;
	
	private int correta;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CadastrarQuestaoObjetivaPresenter(ProvaService provaService,
			QuestaoService questaoService, CadastrarQuestaoObjetivaView view) {
		
		this.provaService = provaService;
		this.questaoService = questaoService;
		this.view = view;
		
		correta = -1;
		
		//para cara checkbox da lista, ele vai adicionar um listener pra
		//verificar se algum foi marcado
		//caso algum tenha sido marcado, ele desmaca aqueles que são diferentes
		for(Checkbox cB : view.getCheckboxList()){
			cB.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChanged(ValueChangeEvent event) {
					Boolean value = (Boolean)(event.getValue());
					if(value.booleanValue()) {
						for(Checkbox c : view.getCheckboxList()) {
							if(c != cB) {
								c.setValue(false);
							}
						}
					} else {
						//aqui ele vai resetar a opcao correta
						//para que nao pege o valor errado,
						//caso ele marque e desmarque a mesma checkbox
						//sem marcar outra
						correta = -1;
					}
				}
				
			});
		}
		
		view.getSalvarButton().addClickListener( e->salvarQuestao(e));
	}
	
	@SuppressWarnings("unchecked")
	private void salvarQuestao(ClickEvent<Button> event) {
		
		Notification notification;
		QuestaoObjetiva questao= new QuestaoObjetiva();
		Prova prova = view.getProva();
		
		/* Criando e armazenando os valores do Input*/
		NivelDificuldade nivelSelecionado = view.getNivelDificuldadeCombo().getValue();
		String enunciado = view.getEnunciado().getValue();
		String justificativa = view.getJustificativaTA().getValue();
		String subarea = view.getSubareaTF().getValue();
		List<String> alternativasList = new ArrayList<String>();
		
		//verifica qual checkbox esta com valor verdadeiro
		//e marca a questao como correta
		for(int i = 0; i < view.getCheckboxList().size(); i++){
			if(view.getCheckboxList().get(i).getValue())
				correta  = i;
		}
		
		//pra nao deixar salvar sem marcar a questao correta
		if(correta == -1) {
			notification = Notification
			        .show("Selecione a alternativa correta.");
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return;
		}
		
		//pegando a lista de alternativas
		for(int i = 0; i < view.getAlternativasList().size(); i++) {
			alternativasList.add(view.getAlternativasList().get(i).getValue());
		}
		
		//verificando se tem alguma alternativa em branco
		for(String s : alternativasList) {
			if(s.isEmpty()) {
				notification = Notification
				        .show("Campos em branco." );
				notification.setPosition(Position.TOP_CENTER);
				notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
				return;
			}
		}
				
		//pra nao deixar salvar sem selecionar o nivel de dificuldade
		if(nivelSelecionado == null) {
			notification = Notification
			        .show("Selecione o nível de dificuladade." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return;
		}

		//verificando demais campos em branco
		if(enunciado.isEmpty() || justificativa.isEmpty() || subarea.isEmpty()) {
			notification = Notification
			        .show("Campos em branco." );
			notification.setPosition(Position.TOP_CENTER);
			notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
			return;
		}
		
		//setando os dados do objeto
		questao.setAlternativaCorreta(correta);
		questao.setAlternativas(alternativasList);
		questao.setConteudoEspecifico(subarea);
		questao.setEnunciado(enunciado);
		questao.setJustificativa(justificativa);
		questao.setNivelDificuldade(nivelSelecionado);
		questao.setQuantAlternativas(view.getQuantAlternativas());
		questao.setProva(view.getProva());
		/*****TEST****
		Revisao revisao = new Revisao();
		revisao.setOrientacoes("Orientatcoes orientadas");
		HashMap<String,Integer> hashMap = new HashMap<>();
		hashMap.put("Paralelismo",2);
		Correcao correcao = new Correcao();
		correcao.setAtendimentoSugestoes(2);
		correcao.setJustificativa("Pq nos gostamos de atendender 2");
		questao.enviarParaRevisao(correcao);
		questao.enviarParaCorrecao(revisao);
		questao.enviarParaRevisao(correcao);
		/*****TEST*****/
		prova.getQuestoes().add(questao);
		
		
		provaService.save(prova);
		
		
		/*Notifica ação bem sucedida*/
		notification = Notification
		        .show("Questão salva com sucesso!");
		notification.setPosition(Position.TOP_CENTER);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}
	
}
