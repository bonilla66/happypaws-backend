package org.ncapas.happypawsbackend.repositories;

import org.ncapas.happypawsbackend.Domain.Entities.Pet_Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetAttributeRepository extends JpaRepository<Pet_Attribute, Integer> {
    List<Pet_Attribute> findAll();
}
