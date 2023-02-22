package br.ufg.sep.state.stateImpl;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;
import br.ufg.sep.state.QuestaoState;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Elaboracao extends QuestaoState {

    public Elaboracao(){
        super();
    }


    @Override
    public Boolean enviarParaRevisao(Questao questao, Correcao correcao/*null*/) {//enviar para REVISAO 1
        // como não veio de uma corrreção, não havera uso do objeto correcao.
        Revisao1 revisao1 = new Revisao1();
    questao.setState(revisao1);
    return true;
    }

    public String toString(){
        return "Em Elaboração";
    }

}
