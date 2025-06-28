package org.ncapas.happypawsbackend.repositories;

import org.ncapas.happypawsbackend.Domain.Entities.Aplication;
import org.ncapas.happypawsbackend.Domain.Entities.Pet;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.Domain.Enums.ApplicationState;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationUserDto;
import org.ncapas.happypawsbackend.services.AplicationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AplicationRepository extends JpaRepository<Aplication, UUID> {

    List<Aplication> findAplicationByUsers (User users);
    //public List<AplicationUserDto> getAcceptedAplicationsByUser(String email);

    List<Aplication> findByUsersEmail(String email);

    List<Aplication> findByUsersEmailAndApplicationState(String email, ApplicationState state);

    List<Aplication> findByApplicationState(ApplicationState state);

    boolean existsByUsersEmailAndPetId(String email, UUID petId);
    List<Aplication> findByPetIdAndApplicationState(UUID petId, ApplicationState state);

    boolean existsByUsersIdAndPetId(UUID userId, UUID petId);


}
