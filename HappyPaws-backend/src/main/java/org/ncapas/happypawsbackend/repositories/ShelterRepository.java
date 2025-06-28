package org.ncapas.happypawsbackend.repositories;

import org.ncapas.happypawsbackend.Domain.Entities.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Integer> {
    Optional<Shelter> findByName(String name);
    boolean existsByName(String name);
}
