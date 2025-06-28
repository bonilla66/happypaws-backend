package org.ncapas.happypawsbackend.Domain.Enums;

import lombok.Getter;

@Getter
public enum ApplicationState {
    PENDIENTE("Pendiente"),
    ACEPTADA("Aceptada"),
    RECHAZADA("Rechazada");

    private final String label;

    ApplicationState(String label) {
        this.label = label;
    }

}
