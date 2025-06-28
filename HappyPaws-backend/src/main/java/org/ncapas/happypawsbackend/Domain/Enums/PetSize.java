package org.ncapas.happypawsbackend.Domain.Enums;

public enum PetSize {
    PEQUEÑO("Pequeño"),
    MEDIANO("Mediano"),
    GRANDE("Grande");

    private final String label;

    PetSize(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

