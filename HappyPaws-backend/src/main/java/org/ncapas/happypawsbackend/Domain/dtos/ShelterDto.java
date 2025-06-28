package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ShelterDto {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
    private String name;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion no puede tener mas de 200 caracteres")
    private String address;

    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{8}$", message = "El telefono debe tener 8 digitos numericos")
    private String phone;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El correo electronico debe tener un formato valido")
    private String email;
}
