package org.ncapas.happypawsbackend.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.dtos.BreedDto;
import org.ncapas.happypawsbackend.Domain.dtos.BreedResponseDto;
import org.ncapas.happypawsbackend.services.BreedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/breeds")
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR', 'ADOPTANTE')")
@RequiredArgsConstructor
public class BreedController {

    private final BreedService breedService;

    @GetMapping("/all")
    public ResponseEntity<List<BreedResponseDto>> getAll() {
        return ResponseEntity.ok(breedService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreedResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(breedService.getById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<BreedResponseDto> create(@Valid @RequestBody BreedDto dto) {
        return ResponseEntity.ok(breedService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BreedResponseDto> update(@PathVariable Integer id, @Valid @RequestBody BreedDto dto) {
        return ResponseEntity.ok(breedService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        String msg = breedService.delete(id);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/byspecie/{id}")
    public ResponseEntity<List<BreedResponseDto>> getBreedsBySpecies(@PathVariable Integer id) {
        System.out.println("Entró a getBreedsBySpecies con id: " + id);
        return ResponseEntity.ok(breedService.getBreedsBySpecies(id));
    }

    @GetMapping("/related/{id}")
    public ResponseEntity<Map<String, Boolean>> checkBreedRelations(@PathVariable Integer id) {
        Map<String, Boolean> result = breedService.checkBreedRelations(id);
        return ResponseEntity.ok(result);
    }


}

