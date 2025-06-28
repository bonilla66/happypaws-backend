package org.ncapas.happypawsbackend.services;

import lombok.NoArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.*;
import org.ncapas.happypawsbackend.Domain.Enums.PetStatus;
import org.ncapas.happypawsbackend.Domain.dtos.PetAttributeResponseDto;
import org.ncapas.happypawsbackend.Domain.dtos.PetPatchDto;
import org.ncapas.happypawsbackend.Domain.dtos.PetRegisterDto;
import org.ncapas.happypawsbackend.Domain.dtos.PetResponse;
import org.ncapas.happypawsbackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetAttributeRepository petAttributeRepository;

    @Autowired
    private ImageRepository imageRepository;


    private PetResponse toPetResponse(Pet pet) {
        List<PetAttributeResponseDto> attributeDtos = pet.getAttributes().stream()
                .map(attr -> PetAttributeResponseDto.builder()
                        .id(attr.getId_pet_attribute())
                        .attributeName(attr.getAttributeName())
                        .attributeValue(attr.getAttributeValue())
                        .build())
                .collect(Collectors.toList());

        // Calcular ageValue y ageUnit desde pet.getAge() (que está en meses)
        int edadEnMeses = pet.getAge();
        String ageUnit = "MESES";
        int ageValue = edadEnMeses;

        if (edadEnMeses % 12 == 0) {
            ageUnit = "AÑOS";
            ageValue = edadEnMeses / 12;
        }

        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .species(pet.getSpecies() != null ? pet.getSpecies().getName() : null)
                .speciesId(pet.getSpecies() != null ? pet.getSpecies().getIdSpecies().longValue() : null)
                .breed(pet.getBreed() != null ? pet.getBreed().getName() : null)
                .breedId(pet.getBreed() != null ? pet.getBreed().getId_breed().longValue() : null)
                .size(pet.getSize() != null && pet.getSize().getName() != null ? pet.getSize().getName().getLabel() : null)
                .sizeId(pet.getSize() != null ? pet.getSize().getId_size().longValue() : null)
                .gender(pet.getGender() != null ? pet.getGender().name() : null)
                .age(pet.getAge()) // en meses
                .ageValue(ageValue) // 2
                .ageUnit(ageUnit)
                .sterilized(pet.isSterilized())
                .parasiteFree(pet.isParasiteFree())
                .fullyVaccinated(pet.isFullyVaccinated())
                .status(pet.getStatus() != null ? pet.getStatus().name() : null)
                .photoUrl(pet.getImage() != null ? pet.getImage().getImgURL() : null)
                .description(pet.getDescription())
                .history(pet.getHistory())
                .entryDate(pet.getEntry_Date() != null ? pet.getEntry_Date().atStartOfDay().toInstant(ZoneOffset.UTC) : null)
                .reviewDate(pet.getReview_Date() != null ? pet.getReview_Date().atStartOfDay().toInstant(ZoneOffset.UTC) : null)
                .shelterId(pet.getShelter() != null ? pet.getShelter().getId_shelter().longValue() : null)
                .attributes(attributeDtos)
                .weight(pet.getWeight())
                .build();
    }



    public List<PetResponse> getAllPets(String status) {
        List<Pet> pets = (status != null)
                ? petRepository.findByStatus(PetStatus.valueOf(status.toUpperCase()))
                : petRepository.findAll();
        return pets.stream()
                .map(this::toPetResponse)
                .collect(Collectors.toList());
    }


    public void createPet(PetRegisterDto register) {
        // Obtener el email del usuario autenticado en el login, ya guarda el id al crear una mascota
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pet pet = new Pet();


        //pet.setId(UUID.randomUUID()); se lo asigne como valor generado en la entidad,
        //ojo no se si vaya a dar error si lo tenian como update el aplicationproperties
        pet.setName(register.getName());

        // Convertir edad a meses
        int edadEnMeses = register.getAgeUnit().equalsIgnoreCase("años")
                ? register.getAgeValue() * 12
                : register.getAgeValue();
        pet.setAge(edadEnMeses);

        pet.setGender(register.getGender());
        pet.setWeight(register.getWeight());
        pet.setSterilized(register.isSterilized());
        pet.setParasiteFree(register.isParasiteFree());
        pet.setFullyVaccinated(register.isFullyVaccinated());
        pet.setEntry_Date(register.getEntryDate());
        pet.setReview_Date(register.getReviewDate());
        pet.setDescription(register.getDescription());
        pet.setHistory(register.getHistory());
        if (register.getImageId() != null) {
            Image image = imageRepository.findById(register.getImageId())
                    .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ese ID"));
            pet.setImage(image);
        }


        pet.setStatus(PetStatus.DISPONIBLE);

        pet.setUser(user);

        pet.setShelter(shelterRepository.findById(register.getShelterId())
                .orElseThrow(() -> new RuntimeException("Shelter no encontrado")));
        pet.setSpecies(speciesRepository.findById(register.getSpeciesId())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada")));
        pet.setSize(sizeRepository.findById(register.getSizeId())
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado")));

        // Raza opcional
        if (register.getBreedId() != null) {
            pet.setBreed(breedRepository.findById(register.getBreedId())
                    .orElseThrow(() -> new RuntimeException("Raza no encontrada")));
        } else {
            pet.setBreed(null);
        }

        if (register.getPetAttributeIds() != null && !register.getPetAttributeIds().isEmpty()) {
            List<Integer> ids = register.getPetAttributeIds();
            List<Pet_Attribute> attributes = petAttributeRepository.findAllById(ids);

            if (attributes.size() != ids.size()) {
                throw new RuntimeException("Uno o más atributos no existen");
            }

            pet.setAttributes(attributes);
        }
        petRepository.save(pet);
    }



    public PetResponse getPetById(UUID id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        return toPetResponse(pet);
    }

    public void deletePet(UUID id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada");
        }
        petRepository.deleteById(id);
    }

    public PetResponse patchPet(UUID id, PetPatchDto dto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (dto.getName() != null) pet.setName(dto.getName());
        if (dto.getAgeValue() != null && dto.getAgeUnit() != null) {
            int edadEnMeses = dto.getAgeUnit().equalsIgnoreCase("AÑOS") ? dto.getAgeValue() * 12 : dto.getAgeValue();
            pet.setAge(edadEnMeses);
        }
        if (dto.getGender() != null) pet.setGender(dto.getGender());
        if (dto.getWeight() != null) pet.setWeight(dto.getWeight());
        if (dto.getSterilized() != null) pet.setSterilized(dto.getSterilized());
        if (dto.getParasiteFree() != null) pet.setParasiteFree(dto.getParasiteFree());
        if (dto.getFullyVaccinated() != null) pet.setFullyVaccinated(dto.getFullyVaccinated());
        if (dto.getEntryDate() != null) pet.setEntry_Date(dto.getEntryDate());
        if (dto.getReviewDate() != null) pet.setReview_Date(dto.getReviewDate());
        if (dto.getDescription() != null) pet.setDescription(dto.getDescription());
        if (dto.getHistory() != null) pet.setHistory(dto.getHistory());
        if (dto.getPhotoURL() != null && !dto.getPhotoURL().isBlank()) {
            Image image = pet.getImage();
            if (image == null) {
                image = new Image();
                image.setId(UUID.randomUUID());
                image.setName("Foto de " + pet.getName());
                image.setImageId("img-" + UUID.randomUUID()); // O algo válido aquí
                pet.setImage(image);
            }
            image.setImgURL(dto.getPhotoURL());
        }

        if (dto.getStatus() != null) pet.setStatus(dto.getStatus());


        if (dto.getBreedId() != null) {
            pet.setBreed(breedRepository.findById(dto.getBreedId())
                    .orElseThrow(() -> new RuntimeException("Raza no encontrada")));
        } else {
            pet.setBreed(null);
        }

        if (dto.getSpeciesId() != null) pet.setSpecies(Species.builder().idSpecies(dto.getSpeciesId()).build());
        if (dto.getSizeId() != null) pet.setSize(Size.builder().id_size(dto.getSizeId()).build());
        if (dto.getShelterId() != null) pet.setShelter(Shelter.builder().id_shelter(dto.getShelterId()).build());

        petRepository.save(pet);

        // esto para actualizar atributos, aunque este nulll
        if (dto.getPetAttributeIds() != null) {
            if (dto.getPetAttributeIds().isEmpty()) {
                pet.setAttributes(List.of()); // Limpiar todos
            } else {
                List<Integer> ids = dto.getPetAttributeIds();
                List<Pet_Attribute> nuevos = petAttributeRepository.findAllById(ids);

                if (nuevos.size() != ids.size()) {
                    throw new RuntimeException("Uno o más atributos no existen");
                }

                pet.setAttributes(nuevos);
            }
        }

        petRepository.save(pet);
        return toPetResponse(pet);
    }


    public List<PetResponse> getPetsByStatus(PetStatus status) {
        List<Pet> pets = petRepository.findByStatus(status);
        return pets.stream().map(this::toPetResponse).collect(Collectors.toList());
    }

    public List<PetResponse> getPetsByUser(UUID userId) {
        List<Pet> pets = petRepository.findByUserId(userId);
        return pets.stream().map(this::toPetResponse).collect(Collectors.toList());
    }

    public List<PetResponse> getVaccinatedPets() {
        List<Pet> pets = petRepository.findByFullyVaccinatedTrue();
        return pets.stream().map(this::toPetResponse).collect(Collectors.toList());
    }

    public List<PetResponse> getSterilizedPets() {
        List<Pet> pets = petRepository.findBySterilizedTrue();
        return pets.stream().map(this::toPetResponse).collect(Collectors.toList());
    }

    public List<PetResponse> getDewormedPets() {
        List<Pet> pets = petRepository.findByParasiteFreeTrue();
        return pets.stream().map(this::toPetResponse).collect(Collectors.toList());
    }

}