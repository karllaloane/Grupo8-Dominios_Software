package br.ufg.sep.views.revisao.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;

public class ComponenteQuestao extends VerticalLayout{
	
	private VerticalLayout questaoLayout;
	private VerticalLayout respEsperadaLayout;
	private VerticalLayout alternativaLayout;
	private List<HorizontalLayout> altLayout;
	
	private TextArea enunciado;
	private TextArea respostaEsperada;
	private TextArea alternativaA;
	private TextArea alternativaB;
	private TextArea alternativaC;
	private TextArea alternativaD;
	private TextArea alternativaE;
	
	public ComponenteQuestao() {
		criaLayoutQuestao();
		criaLayoutRespEsperada();
		criaAlternativasLayout();
	}
	
	private void criaLayoutQuestao() {
		questaoLayout = new VerticalLayout();
				
		VerticalLayout enunciadoLayout = new VerticalLayout();
		
		//criando os componentes do layout de questao
		Span enunciadoSpan = new Span("Enunciado");
		enunciado = new TextArea();
		
		//alterando estilos
		enunciado.setWidthFull();
		enunciado.setMinHeight("90px");

		enunciadoLayout.setPadding(false);
		enunciadoLayout.setSpacing(false);
		enunciadoLayout.add(enunciadoSpan, enunciado);
		
		questaoLayout.setWidth("520px");
		//questaoLayout.setAlignItems(Alignment.CENTER);
		
		questaoLayout.add(enunciadoLayout);
		questaoLayout.setPadding(false);
		
		this.add(questaoLayout);
		
	}
	
	private void criaLayoutRespEsperada() {
		respEsperadaLayout = new VerticalLayout();
				
		VerticalLayout respLayout = new VerticalLayout();
		
		//criando os componentes do layout de questao
		Span enunciadoSpan = new Span("Resposta Esperada");
		respostaEsperada = new TextArea();
		
		//alterando estilos
		respostaEsperada.setWidthFull();
		respostaEsperada.setMinHeight("90px");

		respLayout.setPadding(false);
		respLayout.setSpacing(false);
		respLayout.add(enunciadoSpan, respostaEsperada);
		
		respEsperadaLayout.setWidth("520px");
		//questaoLayout.setAlignItems(Alignment.CENTER);
		
		respEsperadaLayout.add(respLayout);
		respEsperadaLayout.setPadding(false);
		
		this.add(respEsperadaLayout);
		
	}
	
	private void criaAlternativasLayout() {
		alternativaLayout = new VerticalLayout();
		
		//Layout de Label
		Label alternativaLabel = new Label("Alternativas");
			
		HorizontalLayout labelLayout = new HorizontalLayout();
		
		alternativaLabel.setWidth("490px");
		labelLayout.add(alternativaLabel);
		
		
		//lista de span que guarda as alternativas a), b)...
		List<Span> spanList = new ArrayList<Span>();
		
		
		//layout auxiliar para centralizar os checkbox
		List<HorizontalLayout> auxLayout = new ArrayList<HorizontalLayout>();
				
		//layout para guardar a letra, textArea e Checkbox (por alternativa)
		altLayout = new ArrayList<HorizontalLayout>();
		
		//adicionnado o layout de labels
		alternativaLayout.add(labelLayout);
						
		//para popular a lista de Span com as letras das alternativas
		char a = 'a';
		for(int i = 0; i < 5; i++) {
			spanList.add(new Span(a + ")"));
			a++;
		}	
		
		alternativaA = new TextArea();
		alternativaB = new TextArea();
		alternativaC = new TextArea();
		alternativaD = new TextArea();
		alternativaE = new TextArea();
		
		alternativaA.setWidth("490px");
		alternativaB.setWidth("490px");
		alternativaC.setWidth("490px");
		alternativaD.setWidth("490px");
		alternativaE.setWidth("490px");
		
		altLayout.add(new HorizontalLayout());
		altLayout.get(0).setAlignItems(Alignment.CENTER);
		altLayout.get(0).add(spanList.get(0), alternativaA);
		
		altLayout.add(new HorizontalLayout());
		altLayout.get(1).setAlignItems(Alignment.CENTER);
		altLayout.get(1).add(spanList.get(1), alternativaB);
		
		altLayout.add(new HorizontalLayout());
		altLayout.get(2).setAlignItems(Alignment.CENTER);
		altLayout.get(2).add(spanList.get(2), alternativaC);
		
		altLayout.add(new HorizontalLayout());
		altLayout.get(3).setAlignItems(Alignment.CENTER);
		altLayout.get(3).add(spanList.get(3), alternativaD);
		
		altLayout.add(new HorizontalLayout());
		altLayout.get(4).setAlignItems(Alignment.CENTER);
		altLayout.get(4).add(spanList.get(4), alternativaE);

				
		//adicionando os layouts de alternativas individuais ao layout geral
		for(int i = 0; i <5; i++) {	
			alternativaLayout.add(altLayout.get(i));
		}
		
		alternativaLayout.setWidth("554px");
		alternativaLayout.setPadding(false);
		
		this.add(alternativaLayout);

	}
	
	public void ocultarQuintaAlternativa() {
		altLayout.get(4).setVisible(false);
	}
	
	public void bloquearEdicao() {
		enunciado.setReadOnly(true);	
		respostaEsperada.setReadOnly(true);
		alternativaA.setReadOnly(true);	
		alternativaB.setReadOnly(true);	
		alternativaC.setReadOnly(true);	
		alternativaD.setReadOnly(true);	
		alternativaE.setReadOnly(true);	
	}
	
	public void ocutarRespostaEsperada() {
		respEsperadaLayout.setVisible(false);
	}
	
	public TextArea getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(TextArea enunciado) {
		this.enunciado = enunciado;
	}

	public TextArea getRespostaEsperada() {
		return respostaEsperada;
	}

	public void setRespostaEsperada(TextArea respostaEsperada) {
		this.respostaEsperada = respostaEsperada;
	}

	public TextArea getAlternativaA() {
		return alternativaA;
	}

	public void setAlternativaA(TextArea alternativaA) {
		this.alternativaA = alternativaA;
	}

	public TextArea getAlternativaB() {
		return alternativaB;
	}

	public void setAlternativaB(TextArea alternativaB) {
		this.alternativaB = alternativaB;
	}

	public TextArea getAlternativaC() {
		return alternativaC;
	}

	public void setAlternativaC(TextArea alternativaC) {
		this.alternativaC = alternativaC;
	}

	public TextArea getAlternativaD() {
		return alternativaD;
	}

	public void setAlternativaD(TextArea alternativaD) {
		this.alternativaD = alternativaD;
	}

	public TextArea getAlternativaE() {
		return alternativaE;
	}

	public void setAlternativaE(TextArea alternativaE) {
		this.alternativaE = alternativaE;
	}

	public void ocutarAlternativas() {
		alternativaLayout.setVisible(false);
	}
	
}
