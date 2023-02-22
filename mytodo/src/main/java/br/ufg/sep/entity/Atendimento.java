package br.ufg.sep.entity;

public enum Atendimento {
	PARCIAL("Atendimento Parcial"), TOTAL("Atendimento Total"), NAO_ATENDIDA("Não Atende");
	
	private String atendimento;
    private Atendimento(String atendimento) {
        this.atendimento = atendimento;
    }
    
    @Override
    public String toString(){
        return atendimento;
    }
}
