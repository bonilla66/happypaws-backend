package org.ncapas.happypawsbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.Shelter;
import org.ncapas.happypawsbackend.Domain.dtos.ShelterDto;
import org.ncapas.happypawsbackend.Domain.dtos.ShelterResponseDto;
import org.ncapas.happypawsbackend.services.ShelterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping("/all")
    public ResponseEntity<List<ShelterResponseDto>> getAll() {
        return ResponseEntity.ok(shelterService.getAllShelters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelterResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody ShelterDto dto) {
        shelterService.createShelter(dto);
        return ResponseEntity.ok(Map.of("message", "Refugio agregado con exito"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelterResponseDto> updateShelter(@PathVariable Integer id, @Valid @RequestBody ShelterDto dto) {
        ShelterResponseDto response = shelterService.updateShelter(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.ok(Map.of("message", "Refugio eliminado con éxito"));
    }

    @GetMapping("/related/{id}")
    public ResponseEntity<Map<String, Boolean>> checkShelterRelations(@PathVariable Integer id) {
        boolean hasPets = shelterService.hasAssociatedPets(id);
        return ResponseEntity.ok(Map.of("hasPets", hasPets));
    }

}
