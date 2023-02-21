package br.ufg.sep.entity;

public enum NivelProva {
    FUNDAMENTAL("Fundamental"), MEDIO("Médio"),SUPERIOR("Superior");
    
    private String nivel;
    private NivelProva(String nivel) {
        this.nivel = nivel;
    }
    
    @Override
    public String toString(){
        return nivel;
    }
}
