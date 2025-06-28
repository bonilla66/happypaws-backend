package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpeciesDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;
}
