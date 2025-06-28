package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class UserDto {
    private UUID id_user;
    private String name;
    private String email;
    private String DUI;
    private String phone;
    private String rol;
}
