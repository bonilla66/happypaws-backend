package org.ncapas.happypawsbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.ncapas.happypawsbackend.Domain.Entities.Aplication;
import org.ncapas.happypawsbackend.Domain.Entities.Pet;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.Domain.Enums.ApplicationState;
import org.ncapas.happypawsbackend.Domain.Enums.PetStatus;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationRegisterDto;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationResponse;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationUpdateDto;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationUserDto;
import org.ncapas.happypawsbackend.repositories.AplicationRepository;
import org.ncapas.happypawsbackend.repositories.PetRepository;
import org.ncapas.happypawsbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class AplicationService {

    @Autowired
    private AplicationRepository aplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    public void createAplication(AplicationRegisterDto request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + request.getPetId()));


        boolean existsAplicationByPet = aplicationRepository.existsByUsersIdAndPetId(
                user.getId(),
                request.getPetId()
        );



        if (existsAplicationByPet) {
            throw new RuntimeException("Ya existe una solicitud para esta mascota por este usuario");
        }

        Aplication aplication = new Aplication();
        aplication.setOther_Pets(request.isOther_Pets());
        aplication.setLocationDescription(request.getLocationDescription());
        aplication.setEnough_space(request.isEnough_space());
        aplication.setReason_adoption(request.getReason_adoption());
        aplication.setEnough_time(request.isEnough_time());

        aplication.setUsers(user);
        aplication.setPet(pet);

        aplication.setApplicationState(ApplicationState.PENDIENTE);

        aplication.setAplication_Date(new Date());

        aplicationRepository.save(aplication);
    }

    public void deleteAplication(UUID id) {
        if (!aplicationRepository.existsById(id)) {
            throw new RuntimeException("Solicitud no encontrada");
        }
        aplicationRepository.deleteById(id);
    }

    public void updateApplicationState(UUID id, AplicationUpdateDto request) {
        Aplication application = aplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));

        ApplicationState newState = ApplicationState.valueOf(request.getAplicationState());
        //esto hace que si ya la aceptamos o rechazamos no se pueda cambiar ese estadoo
        if (application.getApplicationState() == ApplicationState.ACEPTADA ||
                application.getApplicationState() == ApplicationState.RECHAZADA) {
            throw new IllegalStateException("No se puede modificar una solicitud que ya ha sido " + application.getApplicationState().name());
        }
        application.setApplicationState(newState);
        application.setCompletion_Date(new Date());




        // cambiar el estado de la mascota a adoptado si sera aceptada la soli
        if (newState == ApplicationState.ACEPTADA) {
            Pet pet = application.getPet();
            pet.setStatus(PetStatus.ADOPTADO);
            petRepository.save(pet);

            // rechaza las demas del mismo pet
            List<Aplication> solicitudes = pet.getApplications();
            for (Aplication otherApp : solicitudes) {
                if (!otherApp.getId_aplication().equals(id) &&
                        otherApp.getApplicationState() == ApplicationState.PENDIENTE) {

                    otherApp.setApplicationState(ApplicationState.RECHAZADA);
                    otherApp.setCompletion_Date(new Date());
                    aplicationRepository.save(otherApp);
                }
            }
        }

        aplicationRepository.save(application);

    }

    public List<AplicationResponse> getAllApplications() {
        List<Aplication> applications = aplicationRepository.findAll();
        List<AplicationResponse> dtos = new ArrayList<>();

        for (Aplication ap : applications) {
            AplicationResponse dto = new AplicationResponse();
            dto.setId(ap.getId_aplication());
            dto.setAplicationDate(ap.getAplication_Date());
            dto.setOtherPets(ap.isOther_Pets());
            dto.setReasonAdoption(ap.getReason_adoption());
            dto.setEnoughSpace(ap.isEnough_space());
            dto.setEnoughTime(ap.isEnough_time());
            dto.setLocationDescription(ap.getLocationDescription());
            dto.setAplicationState(String.valueOf(ap.getApplicationState()));

            if (ap.getPet() != null) {
                dto.setPet(ap.getPet().getName());
                dto.setPetId(ap.getPet().getId());
            }

            if (ap.getUsers() != null) {
                dto.setUser(ap.getUsers().getName());
                dto.setUserId(ap.getUsers().getId());
            }

            dtos.add(dto);
        }
        return dtos;
    }

    public AplicationResponse getAplicationById(UUID id) {
        Optional<Aplication> opt = aplicationRepository.findById(id);
        if (opt.isEmpty()) {
            throw new EntityNotFoundException("Aplicación no encontrada con id: " + id);
        }
        Aplication a = opt.get();

        AplicationResponse dto = new AplicationResponse();
        dto.setId(a.getId_aplication());
        dto.setAplicationDate(a.getAplication_Date());
        dto.setOtherPets(a.isOther_Pets());
        dto.setReasonAdoption(a.getReason_adoption());
        dto.setEnoughSpace(a.isEnough_space());
        dto.setEnoughTime(a.isEnough_time());
        dto.setLocationDescription(a.getLocationDescription());
        dto.setAplicationState(String.valueOf(a.getApplicationState()));

        if (a.getPet() != null) {
            dto.setPetId(a.getPet().getId());
            dto.setPet(a.getPet().getName());
        }

        if (a.getUsers() != null) {
            dto.setUserId(a.getUsers().getId());
            dto.setUser(a.getUsers().getName());
        }


        return dto;
    }

    public List<AplicationUserDto> getAplicationsByUser(String email) {
        List<Aplication> solicitudes = aplicationRepository.findByUsersEmail(email);

        return solicitudes.stream().map(ap -> {
            Pet pet = ap.getPet();

            return new AplicationUserDto(
                    pet.getName(),
                    ap.getAplication_Date()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(),
                    ap.getApplicationState(),
                    pet.getGender().name(),
                    pet.getSpecies().getName()
            );
        }).collect(Collectors.toList());
    }

    public String getLoggedUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    public List<AplicationUserDto> getAcceptedAplicationsByLoggedUser() {
        // obtiene un usuario
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Buscar las solicitudes con estado ACEPTADA para ese usuario
        List<Aplication> solicitudes = aplicationRepository
                .findByUsersEmailAndApplicationState(email, ApplicationState.ACEPTADA);

        return solicitudes.stream().map(ap -> {
            Pet pet = ap.getPet();

            return new AplicationUserDto(
                    pet.getName(),
                    ap.getAplication_Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    ap.getApplicationState(),
                    pet.getGender().name(),
                    pet.getSpecies().getName()
            );
        }).collect(Collectors.toList());
    }


}





