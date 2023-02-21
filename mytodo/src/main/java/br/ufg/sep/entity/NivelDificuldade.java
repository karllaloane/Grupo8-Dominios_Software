package br.ufg.sep.entity;

public enum NivelDificuldade {
	FACIL("Fácil"),MEDIO("Médio"),DIFICIL("Difícil");
	
	private String nivel;
    private NivelDificuldade(String nivel) {
        this.nivel = nivel;
    }
    
    @Override
    public String toString(){
        return nivel;
    }
}
