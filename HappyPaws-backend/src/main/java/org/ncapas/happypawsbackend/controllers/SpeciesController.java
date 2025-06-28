package org.ncapas.happypawsbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.dtos.SpeciesDto;
import org.ncapas.happypawsbackend.Domain.dtos.SpeciesResponseDto;
import org.ncapas.happypawsbackend.services.SpeciesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/species")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesService speciesService;

    @GetMapping("/all")
    public ResponseEntity<List<SpeciesResponseDto>> getAll() {
        return ResponseEntity.ok(speciesService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpeciesResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(speciesService.getById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<SpeciesResponseDto> create(@Valid @RequestBody SpeciesDto dto) {
        return ResponseEntity.ok(speciesService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpeciesResponseDto> update(@PathVariable Integer id, @Valid @RequestBody SpeciesDto dto) {
        return ResponseEntity.ok(speciesService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        String response = speciesService.delete(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/related/{id}")
    public ResponseEntity<Map<String, Boolean>> checkSpeciesRelations(@PathVariable Integer id) {
        Map<String, Boolean> result = speciesService.checkSpeciesRelations(id);
        return ResponseEntity.ok(result);
    }

}
