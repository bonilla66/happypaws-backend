package org.ncapas.happypawsbackend.repositories;

import org.ncapas.happypawsbackend.Domain.Entities.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Integer> {
    Optional<Breed> findByName(String name);
    List<Breed> findBySpecies_IdSpecies(Integer id);

    boolean existsBySpecies_IdSpecies(Integer speciesId);

}
