package br.ufg.sep.views.home;

import javax.annotation.security.PermitAll;

import br.ufg.sep.data.repositories.ProvaRepository;
import br.ufg.sep.data.repositories.QuestaoRepository;
import br.ufg.sep.data.repositories.QuestaoStateRepository;
import br.ufg.sep.data.services.QuestaoService;
import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.views.MainLayout;

import java.util.HashMap;

@Route(value="", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll
public class HomeView extends VerticalLayout {
	
	private Button testar = new Button();
	private TextField console = new TextField("Estado");


	private TextField idField = new TextField("IdField");
	private Button passarEstado = new Button("Passar estado");

	private Button enterId = new Button("PutId");

	private Button deletarQuestoes = new Button("Deletar questoes",new Icon(VaadinIcon.ERASER));
	private Long id;

	public HomeView(QuestaoRepository questaoRepository,
					QuestaoService questaoService,
					QuestaoStateRepository questaoStateRepository,
					ProvaRepository provaRepository){
/*
		deletarQuestoes.addClickListener(e->{
			questaoRepository.deleteAll();
		});

		this.setAlignItems(Alignment.CENTER);

		enterId.addClickListener(e->{
			id = Long.valueOf(idField.getValue());

		});


		testar.setText("GetEstado");
		testar.setIcon(new Icon(VaadinIcon.ADJUST));
		testar.addClickListener(e->{

			Questao q = questaoRepository.findById(Long.valueOf(id)).get();

			console.setValue(q.getState().toString());
		});

		this.passarEstado.setIcon(new Icon((VaadinIcon.POINTER)));
		passarEstado.addClickListener(c->{

			Questao q = questaoRepository.findById(Long.valueOf(id)).get();

//			Correcao corr = new Correcao();
//			corr.setAtendimentoSugestoes(AtendimentoSugestoes.PARCIAL);
//			corr.setJustificativa("Teste dia 20.02");

			Revisao rev = new Revisao();
			rev.setOrientacoes("Revisao 20.02");
			HashMap<String,Integer> hashMap = new HashMap<>();
			hashMap.put("Paralelismo",0);
			hashMap.put("Conteudo",0);
			rev.setItemAnalisado(hashMap);

//			q.enviarParaRevisao(null); // go to R1
//			questaoRepository.save(q);

			q.enviarParaBanca(rev); // go to C1
			questaoRepository.save(q);



//			corr.setJustificativa("Agora, analisamos a revisao 1, e decidimos isso 20.02");
//			q.enviarParaRevisao(corr);// go to R2
//			questaoRepository.save(q);
//
//			rev.setOrientacoes("Ainda estou te orientando o quanto a isso 20.02");
//			q.enviarParaBanca(rev);//Go to C2
//			questaoRepository.save(q);
//
//			Correcao correcao2 = new Correcao();
//			correcao2.setJustificativa("Justststs 20.02 - 2");
//			correcao2.setAtendimentoSugestoes(AtendimentoSugestoes.PARCIAL);
//			q.enviarParaRevisao(correcao2); // enviar para R3
//			questaoRepository.save(q);
//
//			Revisao revisola = new Revisao();
//			revisola.setOrientacoes("orientations 20.02 - 2");
//			HashMap<String,Integer> hs = new HashMap<>();
//			hs.put("Interessantismo",2);
//			revisola.setItemAnalisado(hs);
//			q.enviarParaRevisaoLinguagem(revisola);
//			questaoRepository.save(q);
//
//			Revisao revisaoLinguagem = new Revisao();
//			HashMap<String,Integer> xx = new HashMap<>();
//			xx.put("Leiturismo",2);
//			revisaoLinguagem.setItemAnalisado(xx);
//			revisaoLinguagem.setOrientacoes("Por mim mudava tudo, mas so mudei isso viu");
//			q.enviarParaBanca(revisaoLinguagem); // enviar para RBanca
//			questaoRepository.save(q);
//
//			q.concluir(); // concluir
//			questaoRepository.save(q);
//
//			q.guardarNoBanco();
//			questaoRepository.save(q);

		});

		*/
		add(new HorizontalLayout(testar,passarEstado, enterId, deletarQuestoes),idField,console);
	}

	
}