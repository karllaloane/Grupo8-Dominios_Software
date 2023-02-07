package br.ufg.sep.entity;

import java.io.File;

import javax.persistence.Entity;

@Entity
public class AnexoQuestao extends AbstractEntity {
	
	private String tipoAnexo;
	
	private File arquivo;
	
	private String comentario;

	public String getTipoAnexo() {
		return tipoAnexo;
	}

	public void setTipoAnexo(String tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
