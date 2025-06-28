package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreedResponseDto {
    private Integer id_breed;
    private String name;
    private Integer speciesId;
    private String speciesName;
}