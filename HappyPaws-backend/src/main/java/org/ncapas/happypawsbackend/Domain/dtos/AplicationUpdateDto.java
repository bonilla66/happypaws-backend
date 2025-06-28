package org.ncapas.happypawsbackend.Domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class AplicationUpdateDto {

    @NotNull
    private String aplicationState;

}
