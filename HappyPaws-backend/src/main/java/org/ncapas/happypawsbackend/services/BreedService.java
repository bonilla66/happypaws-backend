package org.ncapas.happypawsbackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.Breed;
import org.ncapas.happypawsbackend.Domain.Entities.Species;
import org.ncapas.happypawsbackend.Domain.dtos.BreedDto;
import org.ncapas.happypawsbackend.Domain.dtos.BreedResponseDto;
import org.ncapas.happypawsbackend.repositories.BreedRepository;
import org.ncapas.happypawsbackend.repositories.PetRepository;
import org.ncapas.happypawsbackend.repositories.SpeciesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BreedService {

    private final BreedRepository breedRepository;
    private final SpeciesRepository speciesRepository;
    private final PetRepository petRepository;

    private String normalizeName(String name) {
        if (name == null) return null;
        String trimmed = name.trim().toLowerCase();
        if (trimmed.length() > 3 && trimmed.endsWith("s")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    public List<BreedResponseDto> getAll() {
        return breedRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BreedResponseDto getById(Integer id) {
        Breed breed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
        return toResponseDto(breed);
    }

    @Transactional
    public BreedResponseDto create(BreedDto dto) {
        String normalized = normalizeName(dto.getName());

        boolean exists = breedRepository.findAll().stream()
                .anyMatch(b -> normalizeName(b.getName()).equals(normalized));

        if (exists) {
            throw new RuntimeException("Ya existe una raza con un nombre similar");
        }

        Species species = speciesRepository.findById(dto.getSpeciesId())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        Breed breed = new Breed();
        breed.setName(dto.getName().trim());
        breed.setSpecies(species);

        Breed saved = breedRepository.save(breed);
        return toResponseDto(saved);
    }

    @Transactional
    public BreedResponseDto update(Integer id, BreedDto dto) {
        Breed breed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));

        String normalized = normalizeName(dto.getName());

        boolean exists = breedRepository.findAll().stream()
                .anyMatch(b -> !b.getId_breed().equals(id)
                        && normalizeName(b.getName()).equals(normalized));

        if (exists) {
            throw new RuntimeException("Ya existe otra raza con un nombre similar");
        }

        Species species = speciesRepository.findById(dto.getSpeciesId())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));

        breed.setName(dto.getName().trim());
        breed.setSpecies(species);

        Breed updated = breedRepository.save(breed);
        return toResponseDto(updated);
    }

    @Transactional
    public String delete(Integer id) {
        Breed breed = breedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La raza no existe"));

        if (breed.getPets() != null && !breed.getPets().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: hay mascotas asociadas a esta raza");
        }

        breedRepository.delete(breed);
        return "Raza eliminada correctamente";
    }

    private BreedResponseDto toResponseDto(Breed breed) {
        return new BreedResponseDto(
                breed.getId_breed(),
                breed.getName(),
                breed.getSpecies().getIdSpecies(),
                breed.getSpecies().getName()
        );
    }

    public List<BreedResponseDto> getBreedsBySpecies(Integer speciesId) {
        return breedRepository.findBySpecies_IdSpecies(speciesId).stream()
                .map(breed -> new BreedResponseDto(
                        breed.getId_breed(),
                        breed.getName(),
                        breed.getSpecies().getIdSpecies(),
                        breed.getSpecies().getName()
                ))
                .toList();
    }

    public Map<String, Boolean> checkBreedRelations(Integer breedId) {
        boolean hasPets = petRepository.existsByBreedId(breedId);
        return Map.of("hasPets", hasPets);
    }

}
