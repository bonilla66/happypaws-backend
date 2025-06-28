package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreedDto {
    @NotBlank(message = "El nombre de la raza no puede estar vacío")
    private String name;

    @NotNull(message = "Debe especificar una especie")
    private Integer speciesId;
}
