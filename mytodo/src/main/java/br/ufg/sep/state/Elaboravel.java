package br.ufg.sep.state;

import br.ufg.sep.entity.Correcao;
import br.ufg.sep.entity.Questao;
import br.ufg.sep.entity.Revisao;

public interface Elaboravel {
    public Boolean enviarParaRevisao(Questao questao, Correcao correcao); // jogar pros revisores
    public Boolean enviarParaBanca(Questao questao, Revisao revisao); // jogar pra banca
    public Boolean enviarParaRevisaoLinguagem(Questao questao, Revisao revisao);
    public Boolean concluir(Questao questao); // finalizar processo
    public Boolean guardarNoBanco(Questao questao); // salvar questao no banco
    public Boolean descartar(Questao questao, Revisao revisao); // salvar questao


}
