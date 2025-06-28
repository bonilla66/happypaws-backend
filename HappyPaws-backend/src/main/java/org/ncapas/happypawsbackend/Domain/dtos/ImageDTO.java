package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor

public class ImageDTO {

    private UUID id;
    private String name;
    private String imgURL;

}
