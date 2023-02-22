package br.ufg.sep.views.questoes.componente;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

public class CancelarEdicaoDialog {
	private Dialog dialog;
	private Button cancelarDialogButton;
	private Button descartarDialogButton;
	
	public CancelarEdicaoDialog() {
		dialog = new Dialog();
		dialog.setHeaderTitle("Descartar edição");
		dialog.add("Todo o trabalho será perdido. Deseja realmente descartar a edição?");
		
		cancelarDialogButton = new Button("Cancelar");
		cancelarDialogButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		cancelarDialogButton.getStyle().set("margin-right", "auto");
		
		descartarDialogButton = new Button("Descartar");
		descartarDialogButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		dialog.getFooter().add(cancelarDialogButton);
		dialog.getFooter().add(descartarDialogButton);
	}
	
	public Dialog getDialog() {
		return dialog;
	}

	public Button getcancelarDialogButton() {
		return cancelarDialogButton;
	}

	public Button getDescartarDialogButton() {
		return descartarDialogButton;
	}
}
