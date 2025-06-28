package org.ncapas.happypawsbackend.services;

import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.Pet;
import org.ncapas.happypawsbackend.Domain.Entities.Pet_Attribute;
import org.ncapas.happypawsbackend.Domain.dtos.PetAttributeRequestDto;
import org.ncapas.happypawsbackend.Domain.dtos.PetAttributeResponseDto;
import org.ncapas.happypawsbackend.repositories.PetAttributeRepository;
import org.ncapas.happypawsbackend.repositories.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetAttributeService {

    private final PetAttributeRepository attributeRepository;
    private final PetRepository petRepository;

    public void create(PetAttributeRequestDto dto) {
        Pet_Attribute attr = new Pet_Attribute();
        attr.setAttributeName(dto.getAttributeName());
        attr.setAttributeValue(dto.getAttributeValue());

        attributeRepository.save(attr);
    }

    public void update(Integer id, PetAttributeRequestDto dto) {
        Pet_Attribute attr = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atributo no encontrado"));

        attr.setAttributeName(dto.getAttributeName());
        attr.setAttributeValue(dto.getAttributeValue());

        attributeRepository.save(attr);
    }

    public void delete(Integer id) {
        Pet_Attribute attribute =  attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atributo no encontrado"));

        if (!attribute.getPets().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: hay mascotas que usan este atributo");
        }

        attributeRepository.delete(attribute);
    }

    public List<PetAttributeResponseDto> getAllAttributes() {
        return attributeRepository.findAll().stream()
                .map(attr -> PetAttributeResponseDto.builder()
                        .id(attr.getId_pet_attribute())
                        .attributeName(attr.getAttributeName())
                        .attributeValue(attr.getAttributeValue())
                        .pets(attr.getPets().stream()
                                .map(p -> PetAttributeResponseDto.PetInfo.builder()
                                        .id(p.getId())
                                        .name(p.getName())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public Map<String, Boolean> checkAttributeRelations(Integer attributeId) {
        boolean hasPets = petRepository.existsByAttributeId(attributeId);
        return Map.of("hasPets", hasPets);
    }

}
