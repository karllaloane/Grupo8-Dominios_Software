package br.ufg.sep.views.gerenciarProvas;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.data.services.ProvaService;
import br.ufg.sep.entity.NivelProva;
import br.ufg.sep.entity.Prova;
import br.ufg.sep.entity.TipoProva;
import br.ufg.sep.views.MainLayout;
import br.ufg.sep.views.gerenciarProvas.NovaProvaView;
import br.ufg.sep.views.gerenciarProvas.presenter.NovaProvaPresenter;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value="editar_prova", layout= MainLayout.class)
@PageTitle("Editar Prova")
@RolesAllowed({"ADMIN","PED"})
public class EditarProvaView extends NovaProvaView {

    public EditarProvaView(ProvaService provaService, ConcursoService concursoService,
                           CadastroRepository cadastroRepository){
            super(provaService, concursoService, cadastroRepository);
    }

    private void setarRadioGroupTipo(TipoProva tipo) {
        if(tipo==null)return;
        if(tipo.equals(TipoProva.OBJETIVA_4)){
            this.radioTipoProva.setValue(this.radioButtonItemObjetiva);
            this.radioNivelNumAlternativas.setValue("4");
            this.radioNivelNumAlternativas.setVisible(true);
            return;
        }
        if(tipo.equals(TipoProva.OBJETIVA_5)){
            this.radioTipoProva.setValue(this.radioButtonItemObjetiva);
            this.radioNivelNumAlternativas.setValue("5");
            this.radioNivelNumAlternativas.setVisible(true);
            return;
        }
        if(tipo.equals(TipoProva.DISCUSSIVA)){
            this.radioTipoProva.setValue(this.radioButtonItemDiscussiva);
            return;
        }
        if(tipo.equals(TipoProva.REDACAO)){
            this.radioTipoProva.setValue(this.radioButtonItemRedacao);
        }
    }


    //serve para transformar o enum na string definida para o Item do RadioGroup de nivel da prova
    private String valorDoNivel(NivelProva nivel) {
        if(nivel==null)return "";
        if(nivel.equals(NivelProva.FUNDAMENTAL)) return radioButtonItemNivelFundamental;
        if(nivel.equals(NivelProva.MEDIO)) return  radioButtonItemNivelMedio;
        if(nivel.equals(NivelProva.SUPERIOR)) return radioButtonItemNivelSuperior;
        return "";
    }
    @Override
    public void setParameter(BeforeEvent event, Long parameter) { //Método executado após o construtor.
        ParameterId = parameter;
            concurso = provaService.getRepository().findById(parameter).get().getConcurso();
        nomeConcurso.setValue(concurso.getNome());
        this.presenter = new NovaProvaPresenter(this, provaService);

        Prova current = provaService.getRepository().findById(this.ParameterId).get();
        this.nomeConcurso.setValue(current.getConcurso().getNome());
        this.areaConhecimento.setValue(current.getAreaConhecimento());
        this.numQuestoes.setValue(String.valueOf(current.getNumeroQuestoes()));
        this.descricaoDaProva.setValue(current.getDescricao());
        this.prazo.setValue(current.getDataEntrega());
        // pega o nivel e o transforma em item do raidoGroup
        this.radioNivelProva.setValue(valorDoNivel(current.getNivel()));
        this.setarRadioGroupTipo(current.getTipo());
        this.comboBoxMembroBancaQuestao.setValue(current.getElaborador());
        this.comboBoxMembroRevisorLinguagem.setValue(current.getRevisorLinguagem());
        this.comboBoxMembroRevisorTecnico1.setValue(current.getRevisor1());
        this.comboBoxMembroRevisorTecnico2.setValue(current.getRevisor2());
        this.comboBoxMembroRevisorTecnico3.setValue(current.getRevisor3());
    }





}
