package org.ncapas.happypawsbackend.Domain.dtos;


import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto2 {
    private String name;

    @Email(message = "Formato de correo inválido")
    private String email;

    private String DUI;
    private String phone;
    private String rol;
}
