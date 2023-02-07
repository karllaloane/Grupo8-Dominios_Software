package br.ufg.sep.views.gerenciarProvas.presenter;

import br.ufg.sep.views.gerenciarProvas.NovaProvaView;

public class NovaProvaPresenter {
    NovaProvaView view;
    String nomeCadastro="";

    Long cadastroId;

    public NovaProvaPresenter( NovaProvaView view){
        this.view = view;
        view.getElaboradoresGrid().addItemClickListener(item->{
            cadastroId = item.getItem().getId();

            view.getColaboradorAssociado().setValue(
                    view.getCadastroRepository().findById(cadastroId).get().getNome()
            );
        });
        /*********************SALVAR BUTTON***************/
        view.getSalvarButton().addClickListener(click->{



        });

    }


}
