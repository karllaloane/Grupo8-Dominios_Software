package br.ufg.sep.views.concurso;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.ufg.sep.data.repositories.CadastroRepository;
import br.ufg.sep.entity.Cadastro;
import br.ufg.sep.security.SecurityService;
import br.ufg.sep.test.TestView;
import br.ufg.sep.views.MainLayout;

@Route(value="concursos", layout = MainLayout.class)
@PageTitle("Concursos")
@PermitAll
public class ConcursosView extends HorizontalLayout{
	
	public ConcursosView(SecurityService secutiryService, CadastroRepository cr){
	
		HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);
        
        Button novoButton = new Button("Novo", new Icon(VaadinIcon.PLUS));
        
        layout.add(novoButton);
        
        novoButton.addClickListener(e->{
			 novoButton.getUI().ifPresent(ui->{
			 ui.navigate(FormularioConcursoView.class); });

		});
        
        
		

        this.add(layout);
	}

}
