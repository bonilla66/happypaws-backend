package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpeciesResponseDto {
    private Integer id_species;
    private String name;
}

