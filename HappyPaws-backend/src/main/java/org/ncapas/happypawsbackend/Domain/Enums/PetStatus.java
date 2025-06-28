package org.ncapas.happypawsbackend.Domain.Enums;

public enum PetStatus {
    DISPONIBLE("Disponible"),
    ADOPTADO("Adoptado");

    private final String label;

    PetStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
