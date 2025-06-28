package org.ncapas.happypawsbackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.Species;
import org.ncapas.happypawsbackend.Domain.dtos.SpeciesDto;
import org.ncapas.happypawsbackend.Domain.dtos.SpeciesResponseDto;
import org.ncapas.happypawsbackend.repositories.BreedRepository;
import org.ncapas.happypawsbackend.repositories.PetRepository;
import org.ncapas.happypawsbackend.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final PetRepository petRepository;
    private final BreedRepository breedRepository;


    public List<SpeciesResponseDto> getAll() {
        return speciesRepository.findAll().stream()
                .map(species -> new SpeciesResponseDto(species.getIdSpecies(), species.getName()))
                .toList();
    }

    public SpeciesResponseDto getById(Integer id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada con ID: " + id));
        return new SpeciesResponseDto(species.getIdSpecies(), species.getName());
    }

    public SpeciesResponseDto create(SpeciesDto dto) {
        String normalized = normalizeName(dto.getName());

        boolean exists = speciesRepository.findAll().stream()
                .anyMatch(s -> normalizeName(s.getName()).equals(normalized));

        if (exists) {
            throw new RuntimeException("Ya existe una especie con un nombre similar");
        }

        Species species = new Species();
        species.setName(dto.getName().trim());
        Species saved = speciesRepository.save(species);

        return new SpeciesResponseDto(saved.getIdSpecies(), saved.getName());
    }

    public SpeciesResponseDto update(Integer id, SpeciesDto dto) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada con ID: " + id));

        String normalized = normalizeName(dto.getName());

        boolean exists = speciesRepository.findAll().stream()
                .anyMatch(s -> !s.getIdSpecies().equals(id)
                        && normalizeName(s.getName()).equals(normalized));

        if (exists) {
            throw new RuntimeException("Ya existe otra especie con un nombre similar");
        }

        species.setName(dto.getName().trim());
        Species updated = speciesRepository.save(species);

        return new SpeciesResponseDto(updated.getIdSpecies(), updated.getName());
    }

    @Transactional
    public String delete(Integer id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        if ((species.getBreeds() != null && !species.getBreeds().isEmpty()) ||
                (species.getPets() != null && !species.getPets().isEmpty())) {
            throw new RuntimeException("No se puede eliminar: hay razas o mascotas asociadas a esta especie");
        }

        speciesRepository.delete(species);
        return "Especie eliminada correctamente";
    }

    private String normalizeName(String name) {
        if (name == null) return null;

        String trimmed = name.trim().toLowerCase();

        if (trimmed.length() > 3 && trimmed.endsWith("s")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }

        return trimmed;
    }

    public Map<String, Boolean> checkSpeciesRelations(Integer speciesId) {
        boolean hasPets = petRepository.existsBySpecies_IdSpecies(speciesId);
        boolean hasBreeds = breedRepository.existsBySpecies_IdSpecies(speciesId);
        return Map.of(
                "hasPets", hasPets,
                "hasBreeds", hasBreeds
        );
    }


}
