package org.ncapas.happypawsbackend.repositories;

import org.ncapas.happypawsbackend.Domain.Entities.Size;
import org.ncapas.happypawsbackend.Domain.Enums.PetSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    Optional<Size> findByName(PetSize name);
}
