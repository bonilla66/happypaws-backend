package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.*;
import org.ncapas.happypawsbackend.Domain.Enums.ApplicationState;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AplicationUserDto {
        private String pet;
        private LocalDate aplicationDate;
        private ApplicationState state;
        private String gender;
        private String specie;

        public AplicationUserDto(String name, LocalDate localDate, String label, String name1, String name2) {
        }
}
