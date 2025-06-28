package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "El DUI no puede estar vacio")
    @Pattern(regexp = "^\\d{8}-\\d{1}$", message = "El DUI debe tener el formato 12345678-9")
    private String dui;

    @NotBlank(message = "El telefono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{8}$", message = "El telefono debe tener 8 digitos numericos")
    private String phone;

    @NotBlank(message = "El correo no puede estar vacio")
    @Email(message = "El correo debe tener un formato valido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúscula, minúscula, número y símbolo")
    private String password;
}
