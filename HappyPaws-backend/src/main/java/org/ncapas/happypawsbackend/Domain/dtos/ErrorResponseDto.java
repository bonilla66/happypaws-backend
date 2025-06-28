package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Data
@Getter
@AllArgsConstructor
public class ErrorResponseDto {

        private Instant timestamp;
        private int status;
        private String message;
        private String path;
        private Map<String, String> errors;
    }

