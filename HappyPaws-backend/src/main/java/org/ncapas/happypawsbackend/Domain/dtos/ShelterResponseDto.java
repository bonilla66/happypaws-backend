package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.Data;

@Data
public class ShelterResponseDto {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String email;
}
