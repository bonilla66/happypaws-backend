package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnumResponseDto {
    private String value;
    private String label;
}
