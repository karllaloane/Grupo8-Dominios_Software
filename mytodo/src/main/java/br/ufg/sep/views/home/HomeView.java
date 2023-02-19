package br.ufg.sep.views.home;

import javax.annotation.security.PermitAll;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.data.repositories.QuestaoStateRepository;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.views.MainLayout;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

@Route(value="", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll
public class HomeView extends VerticalLayout {
	
	private Button testar = new Button();
	private TextField console = new TextField();

	private Button passarEstado = new Button("Passar estado");

	private Button limparBanco  = new Button("Limpar banco");

	public HomeView(QuestaoRepository questaoRepository,
					QuestaoService questaoService,
					QuestaoStateRepository questaoStateRepository){

		this.setAlignItems(Alignment.CENTER);




		testar.setText("GetEstado");
		testar.setIcon(new Icon(VaadinIcon.ADJUST));
		testar.addClickListener(e->{
			Questao q = questaoRepository.findById(Long.valueOf("272")).get();
			console.setValue(q.getState().toString());
		});

		this.passarEstado.setIcon(new Icon((VaadinIcon.POINTER)));
		passarEstado.addClickListener(c->{
			Questao q = questaoRepository.findById(Long.valueOf("272")).get();
			Correcao corr = new Correcao();
			corr.setAtendimentoSugestoes(2);
			corr.setJustificativa("Paralelismo arrumador");

			Revisao rev = new Revisao();
			rev.setOrientacoes("Revisao");
			HashMap<String,Integer> hashMap = new HashMap<>();
			hashMap.put("Contexto",0);
			rev.setItemAnalisado(hashMap);

			q.enviarParaRevisao(corr); // go to R1
			questaoService.salvarEnvio(q);

			q.enviarParaCorrecao(rev); // go to C1
			questaoService.salvarEnvio(q);



			corr.setJustificativa("Agora, analisamos a revisao 1, e decidimos isso");
			q.enviarParaRevisao(corr);// go to R2
			questaoService.salvarEnvio(q);

			rev.setOrientacoes("Ainda estou te orientando o quanto a isso");
			q.enviarParaCorrecao(rev);//Go to C2
			questaoService.salvarEnvio(q);


		});

		
		add(new HorizontalLayout(testar,passarEstado,limparBanco),console);
	}

	
}