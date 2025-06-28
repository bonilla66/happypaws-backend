package org.ncapas.happypawsbackend.Domain.Enums;

public enum UserRol {
    ADMIN("Administrador"),
    COLABORADOR("Colaborador"),
    ADOPTANTE("Adoptante");

    private final String label;

    UserRol(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
