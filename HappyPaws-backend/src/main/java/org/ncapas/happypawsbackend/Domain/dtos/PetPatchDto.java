package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.Data;
import org.ncapas.happypawsbackend.Domain.Enums.Gender;
import org.ncapas.happypawsbackend.Domain.Enums.PetStatus;

import java.time.LocalDate;
import java.util.List;

@Data
public class PetPatchDto {
    private String name;
    private Integer ageValue;
    private String ageUnit; // "AÑOS" o "MESES"
    private Gender gender;
    private Integer weight;
    private Boolean sterilized;
    private Boolean parasiteFree;
    private Boolean fullyVaccinated;
    private LocalDate entryDate;
    private LocalDate reviewDate;
    private String description;
    private String history;
    private String photoURL;
    private PetStatus status;
    private Integer shelterId;
    private Integer breedId;
    private Integer speciesId;
    private Integer sizeId;
    private List<Integer> petAttributeIds;

}
