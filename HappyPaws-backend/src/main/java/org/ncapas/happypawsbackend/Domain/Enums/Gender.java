package org.ncapas.happypawsbackend.Domain.Enums;

public enum Gender {
    MACHO("Macho"),
    HEMBRA("Hembra");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
