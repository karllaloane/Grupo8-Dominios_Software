package br.ufg.sep.views.questoes.componente;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

public class ConfirmaEnvioRevisaoDialog extends Dialog{

	private Dialog dialog;
	private Button cancelarDialogButton;
	private Button enviarDialogButton;
	
	public ConfirmaEnvioRevisaoDialog() {
		dialog = new Dialog();
		dialog.setHeaderTitle("Enviar para revisão");
		dialog.add("Deseja realmente enviar esta questão para revisão?");
		
		cancelarDialogButton = new Button("Cancelar");
		cancelarDialogButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		cancelarDialogButton.getStyle().set("margin-right", "auto");
		
		enviarDialogButton = new Button("Enviar");
		enviarDialogButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		dialog.getFooter().add(cancelarDialogButton);
		dialog.getFooter().add(enviarDialogButton);
	}
	
	public Dialog getDialog() {
		return dialog;
	}

	public Button getcancelarDialogButton() {
		return cancelarDialogButton;
	}

	public Button getEnviarDialogButton() {
		return enviarDialogButton;
	}
}
