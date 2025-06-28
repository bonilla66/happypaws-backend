package org.ncapas.happypawsbackend.repositories;

import org.ncapas.happypawsbackend.Domain.Entities.Pet;
import org.ncapas.happypawsbackend.Domain.Enums.PetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    //estado
    List<Pet> findByStatus(PetStatus status);

    // creador
    List<Pet> findByUserId(UUID userId);

    // vacunados
    List<Pet> findByFullyVaccinatedTrue();

    // esterilizados
    List<Pet> findBySterilizedTrue();

    // desparasitados
    List<Pet> findByParasiteFreeTrue();

    @Query("SELECT COUNT(p) > 0 FROM Pet p WHERE p.breed.id_breed = :breedId")
    boolean existsByBreedId(@Param("breedId") Integer breedId);

    boolean existsBySpecies_IdSpecies(Integer speciesId);

    @Query("SELECT COUNT(p) > 0 FROM Pet p JOIN p.attributes a WHERE a.id_pet_attribute = :attributeId")
    boolean existsByAttributeId(@Param("attributeId") Integer attributeId);

    @Query("SELECT COUNT(p) > 0 FROM Pet p WHERE p.shelter.id_shelter = :shelterId")
    boolean existsByShelterId(@Param("shelterId") Integer shelterId);

}
