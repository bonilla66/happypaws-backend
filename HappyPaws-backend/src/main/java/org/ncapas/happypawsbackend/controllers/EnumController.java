package org.ncapas.happypawsbackend.controllers;

import org.ncapas.happypawsbackend.Domain.Enums.*;
import org.ncapas.happypawsbackend.Domain.dtos.EnumResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/enums")
public class EnumController {

    @GetMapping("/genders")
    public ResponseEntity<List<EnumResponseDto>> getGenders() {
        List<EnumResponseDto> response = Arrays.stream(Gender.values())
                .map(g -> new EnumResponseDto(g.name(), g.getLabel()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<EnumResponseDto>> getStatuses() {
        List<EnumResponseDto> response = Arrays.stream(PetStatus.values())
                .map(s -> new EnumResponseDto(s.name(), s.getLabel()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sizes")
    public ResponseEntity<List<EnumResponseDto>> getSizes() {
        List<EnumResponseDto> response = Arrays.stream(PetSize.values())
                .map(s -> new EnumResponseDto(s.name(), s.getLabel()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<EnumResponseDto>> getRoles() {
        List<EnumResponseDto> response = Arrays.stream(UserRol.values())
                .map(rol -> new EnumResponseDto(rol.name(), rol.getLabel()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/application-states")
    public ResponseEntity<List<EnumResponseDto>> getApplicationStates() {
        List<EnumResponseDto> response = Arrays.stream(ApplicationState.values())
                .map(e -> new EnumResponseDto(e.name(), e.getLabel()))
                .toList();
        return ResponseEntity.ok(response);
    }
}


