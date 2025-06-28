package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginDto {

    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String password;
}
