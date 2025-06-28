package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PetAttributeRequestDto {
    private String attributeName;
    private String attributeValue;
}