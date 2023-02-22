package br.ufg.sep.views.gerencia.presenter;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;

import br.ufg.sep.data.services.ConcursoService;
import br.ufg.sep.entity.Concurso;
import br.ufg.sep.views.gerencia.ConcursosView;
import br.ufg.sep.views.gerencia.EditarConcursoView;
import br.ufg.sep.views.gerencia.FormularioConcursoView;
import br.ufg.sep.views.gerencia.VisualizarConcursoView;
import br.ufg.sep.views.gerenciarProvas.GerenciarProvasView;

public class ConcursoPresenter {
	
	ConcursoService concursoService;
	Concurso concurso;
	LocalDateRenderer<Concurso> renderizadorDatasConcurso;
			
	public ConcursoPresenter(ConcursosView view, ConcursoService service) 
	{
		this.concursoService = service;
		renderizadorDatasConcurso = new LocalDateRenderer<>(Concurso::getDataFim,"dd/MM/yyyy");
		/***********************GRID************************/
		Grid<Concurso> concursos = new Grid<>(Concurso.class,false);
		concursos.setItems(query->
		service.getRepository().findAll(PageRequest.of(query.getOffset(), 
				query.getLimit())).stream());
		view.setGrid(concursos);
		
		
		view.getGrid().addSelectionListener(selection -> {
			Optional<Concurso> optionalConcurso = selection.getFirstSelectedItem();
            if (optionalConcurso.isPresent()) {
            	Long teste = optionalConcurso.get().getId();
            	concurso = optionalConcurso.get();
            	view.habilitarButtons();
            }
		});
		/***********************GRID************************/

		
		/*** + Novo ****/
		view.getNovoButton().addClickListener(e->{
			view.getNovoButton().getUI().ifPresent(ui->{
			 ui.navigate(FormularioConcursoView.class); });
		});
	

		/*Visualizar*/
		view.getVisualizarButton().addClickListener(e->{
						
			view.getVisualizarButton().getUI().ifPresent(ui->{
				 ui.navigate(VisualizarConcursoView.class, concurso.getId());});
		});
		
		/*Editar*/
		view.getEditarButton().addClickListener(e->{
			
			view.getEditarButton().getUI().ifPresent(ui->{
				 ui.navigate(EditarConcursoView.class, concurso.getId());});
		});
		
		/*Acessar provas*/
		view.getAcessarProvasButton().addClickListener(e->{


			view.getAcessarProvasButton().getUI().ifPresent(ui->{
				 ui.navigate(GerenciarProvasView.class, concurso.getId());});
		});

		/*Deletar concurso*/
		view.getDeletarConcurso().addClickListener(clcik->{
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.setRejectable(true);
			confirmDialog.setRejectText("Não");
			confirmDialog.setConfirmText("Sim");
			confirmDialog.setText("Você tem certeza de que deseja apagar: "+concurso.getNome()+"?");
			if(concurso.getProvas().size()>0) {
				confirmDialog.setHeader("Esse concurso possui provas associadas a ele");
				confirmDialog.setText("Você tem certeza de que deseja apagá-lo?");
			}
			confirmDialog.addConfirmListener(confirm-> {
				service.getRepository().deleteById(concurso.getId());
				view.getConcursos().setItems(query->
						service.getRepository().findAll(PageRequest.of(query.getOffset(),
								query.getLimit())).stream());
			});
			confirmDialog.open();
		});
		
	}
	public LocalDateRenderer<Concurso> getRenderizadorDatasConcurso() {
		return renderizadorDatasConcurso;
	}
	public void setRenderizadorDatasConcurso(LocalDateRenderer<Concurso> renderizadorDatasConcurso) {
		this.renderizadorDatasConcurso = renderizadorDatasConcurso;
	}
	


}
