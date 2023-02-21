package br.ufg.sep.entity;

public enum TipoProva {
    OBJETIVA_4 ("Objetiva"),OBJETIVA_5 ("Objetiva"),DISCUSSIVA ("Discursiva"),REDACAO ("Redação");
	
	private String tipo;
    private TipoProva(String tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString(){
        return tipo;
    }
}
