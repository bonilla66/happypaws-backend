package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.*;
import org.ncapas.happypawsbackend.Domain.Entities.Pet;
import org.ncapas.happypawsbackend.Domain.Entities.User;

import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AplicationResponse {

    private UUID id;
    private UUID userId;
    private UUID petId;
    private String pet;
    private String user;
    private Boolean otherPets;
    private String locationDescription;
    private Date aplicationDate;
    private Boolean enoughSpace;
    private String reasonAdoption;
    private Boolean enoughTime;
    private String aplicationState;

}

