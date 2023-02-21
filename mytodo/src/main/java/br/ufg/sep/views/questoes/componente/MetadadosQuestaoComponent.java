package br.ufg.sep.views.questoes.componente;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import br.ufg.sep.entity.NivelDificuldade;

public class MetadadosQuestaoComponent extends VerticalLayout {
	
	//inputs gerais
	private TextField subareaTextField;
	private ComboBox<NivelDificuldade> nivelDificuldadeCombo;

	private Button adicionarButton;
	final private Grid<String> grid;
	private Button addButton;
	
	private VerticalLayout subAreaLayout;
	private HorizontalLayout addSubAreaLayout;

	private List<String> subAreas;

	@SuppressWarnings("unchecked")
	public MetadadosQuestaoComponent(){
		
		subAreas = new ArrayList<>();
		
		addSubAreaLayout = new HorizontalLayout();
		addSubAreaLayout.setPadding(false);
		
		subAreaLayout = new VerticalLayout();
		subAreaLayout.setPadding(false);
		addSubAreaLayout.setWidthFull();
		subAreaLayout.setWidth("665px");
	
		subareaTextField = new TextField();
		subareaTextField.setTooltipText("Informe a subárea");
		subareaTextField.setWidthFull();
		
		addButton = new Button("Adicionar");
		addButton.setWidth("150px");

		//criação do combobx
		nivelDificuldadeCombo = new ComboBox<>("Nível de dificuldade");
		nivelDificuldadeCombo.setItems(EnumSet.allOf(NivelDificuldade.class));

		addSubAreaLayout.add(subareaTextField, addButton);
		
		grid = new Grid<>();
		grid.setWidthFull();
		
		subAreaLayout.add(new Span("Subárea da questão"), grid, addSubAreaLayout);
		
		grid.setItems(subAreas);
		grid.setAllRowsVisible(true);
		
		//adicionando a coluna
		//conterá o arraylist e o botão de remover
		grid.addColumn(item -> item).setFlexGrow(1);
		grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, item) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_TERTIARY);
                    button.setIcon(new Icon(VaadinIcon.MINUS_CIRCLE_O));
                    button.addClickListener(e -> this.removeSubArea(item));
                })).setWidth("130px").setFlexGrow(0).setKey("remover");

		//um pequeno listener pra adicionar as subareas
		//no grid
		addButton.addClickListener(e ->{
			if(!this.subareaTextField.isEmpty()) {
				this.subAreas.add(subareaTextField.getValue());
			}
			subareaTextField.clear();
			atualizaGrid();
		});
		
		if(subAreas.size() == 0) {
			grid.setVisible(false);
		} else {
			grid.setVisible(true);
		}
		
		this.setPadding(false);
		this.add(subAreaLayout, nivelDificuldadeCombo);
		
	}
	
	//atualizar o grid
	public void atualizaGrid() {
		if(subAreas.size() == 0) {
			grid.setVisible(false);
		} else {
			grid.setItems(this.subAreas);
			grid.getDataProvider().refreshAll();
			grid.setVisible(true);
		}
	}

	//metodo pra remover uma subarea da lista
	private void removeSubArea(String item) {
		subAreas.remove(item);
		atualizaGrid();
	}
	
	//pra poder setar a edicao falta quando 
	//for apenas visualização
	public void setEdicaoFalse() {
		this.addButton.setVisible(false);
		this.subareaTextField.setVisible(false);
		this.addSubAreaLayout.setVisible(false);
		this.nivelDificuldadeCombo.setReadOnly(true);
		grid.getColumnByKey("remover").setVisible(false);;
	}

	public Button getAdicionarButton() {
		return adicionarButton;
	}
	
	public List<String> getSubAreas() {
		return subAreas;
	}
	
	public void setSubAreas(List<String> subAreas) {
		this.subAreas = subAreas;
	}

	public ComboBox<NivelDificuldade> getNivelDificuldadeCombo() {
		return nivelDificuldadeCombo;
	}

	public void setNivelDificuldadeCombo(ComboBox<NivelDificuldade> nivelDificuldadeCombo) {
		this.nivelDificuldadeCombo = nivelDificuldadeCombo;
	}

}
