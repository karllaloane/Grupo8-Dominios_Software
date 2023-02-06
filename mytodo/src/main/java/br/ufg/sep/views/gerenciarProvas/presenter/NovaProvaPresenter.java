package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.data.repositories.ConcursoRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.List;

public class NovaProvaPresenter {
    NovaProvaView view;
    String nomeCadastro="";

    Cadastro cadastro;

    public NovaProvaPresenter( NovaProvaView view){
        this.view = view;
        view.getColaboradoresGrid().addItemClickListener(item->{
            cadastro = item.getItem();

            view.getColaboradorAssociado().setValue(cadastro.getNome());
        });
        /*********************SALVAR BUTTON***************/
        view.getSalvarButton().addClickListener(click->{
                Prova prova = new Prova();

            Concurso concurso = view.getConcurso();

            prova.setAreaConhecimento(view.getAreaConhecimento().getValue());

                prova.setNumeroQuestoes(Integer.parseInt(view.getNumQuestoes().getValue()));

                /********REFATORAR*********/
                prova.setResponsavel(cadastro);
                prova.setConcurso(concurso);
                concurso.addProvas(List.of(prova));
                view.getConcursoService().save(concurso);


        });

    }


}
