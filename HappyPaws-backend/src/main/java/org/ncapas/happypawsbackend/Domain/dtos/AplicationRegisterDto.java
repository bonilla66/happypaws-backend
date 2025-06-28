package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.ncapas.happypawsbackend.Domain.Enums.ApplicationState;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class AplicationRegisterDto {


    @NotNull(message = "Necesitas elegir una opción")
    private boolean other_Pets;

    @NotEmpty(message = "La razón de la adopción es obligatoria")
    private String reason_adoption;

    @NotNull(message = "Necesitas elegir una opción")
    private boolean enough_space;

    @NotNull(message = "Necesitas elegir una opción")
    private boolean enough_time;

    @NotEmpty(message = "La ubicación es obligatoria")
    private String locationDescription;

    private UUID petId;

    private String email;

}
