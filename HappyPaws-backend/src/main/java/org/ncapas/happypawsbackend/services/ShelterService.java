package org.ncapas.happypawsbackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.Shelter;
import org.ncapas.happypawsbackend.Domain.dtos.ShelterDto;
import org.ncapas.happypawsbackend.Domain.dtos.ShelterResponseDto;
import org.ncapas.happypawsbackend.repositories.PetRepository;
import org.ncapas.happypawsbackend.repositories.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;

    private final PetRepository petRepository;

    private String normalizeName(String name) {
        return name == null ? null : name.trim().toLowerCase();
    }

    public List<ShelterResponseDto> getAllShelters() {
        return shelterRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ShelterResponseDto getShelterById(Integer id) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refugio no encontrado"));
        return toResponseDto(shelter);
    }


    @Transactional
    public Shelter createShelter(ShelterDto dto) {
        String normalized = normalizeName(dto.getName());

        boolean exists = shelterRepository.findAll().stream()
                .anyMatch(s -> normalizeName(s.getName()).equals(normalized));

        if (exists) {
            throw new RuntimeException("Ya existe un refugio con un nombre similar");
        }

        Shelter shelter = new Shelter();
        shelter.setName(dto.getName());
        shelter.setAddress(dto.getAddress());
        shelter.setPhone(Integer.parseInt(dto.getPhone()));
        shelter.setEmail(dto.getEmail());

        return shelterRepository.save(shelter);
    }

    private ShelterResponseDto toResponseDto(Shelter shelter) {
        ShelterResponseDto dto = new ShelterResponseDto();
        dto.setId(shelter.getId_shelter());
        dto.setName(shelter.getName());
        dto.setAddress(shelter.getAddress());
        dto.setPhone(String.valueOf(shelter.getPhone()));
        dto.setEmail(shelter.getEmail());
        return dto;
    }

    @Transactional
    public ShelterResponseDto updateShelter(Integer id, ShelterDto dto) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refugio no encontrado"));

        String normalized = normalizeName(dto.getName());

        boolean exists = shelterRepository.findAll().stream()
                .anyMatch(s -> !s.getId_shelter().equals(id) &&
                        normalizeName(s.getName()).equals(normalized));

        if (exists) {
            throw new RuntimeException("Ya existe otro refugio con un nombre similar");
        }

        shelter.setName(dto.getName().trim());
        shelter.setAddress(dto.getAddress());
        shelter.setPhone(Integer.parseInt(dto.getPhone()));
        shelter.setEmail(dto.getEmail());

        Shelter updated = shelterRepository.save(shelter);
        return toResponseDto(updated);
    }


    @Transactional
    public void deleteShelter(Integer id) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refugio no encontrado"));

        if (shelter.getPets() != null && !shelter.getPets().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: hay mascotas asociadas a este refugio");
        }

        shelterRepository.delete(shelter);
    }

    public boolean hasAssociatedPets(Integer shelterId) {
        return petRepository.existsByShelterId(shelterId);
    }

}

