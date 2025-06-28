package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import org.ncapas.happypawsbackend.Domain.Entities.Breed;
import org.ncapas.happypawsbackend.Domain.Enums.Gender;
import org.ncapas.happypawsbackend.Domain.Enums.PetStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PetRegisterDto {

    @NotBlank
    private String name;

    @Min(0)
    private int ageValue;

    @NotBlank
    @Pattern(regexp = "AÑOS|MESES", message = "La unidad de edad debe ser 'AÑOS' o 'MESES'")
    private String ageUnit;

    @NotNull(message = "El genero es obligatorio")
    private Gender gender;

    @Min(0)
    private int weight;

    private boolean sterilized;
    private boolean parasiteFree;
    private boolean fullyVaccinated;

    @NotNull
    private LocalDate entryDate;

    @NotNull
    private LocalDate reviewDate;

    @NotBlank
    private String description;

    @NotBlank
    private String history;

    private UUID imageId;


    private PetStatus status;

    @NotNull
    private Integer shelterId;

    private Integer breedId;

    @NotNull
    private Integer speciesId;

    @NotNull
    private Integer sizeId;

    private List<Integer> petAttributeIds;
}
