package org.ncapas.happypawsbackend.controllers;

import jakarta.validation.Valid;
import org.ncapas.happypawsbackend.Domain.Entities.Aplication;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.Domain.Enums.ApplicationState;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationRegisterDto;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationResponse;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationUpdateDto;
import org.ncapas.happypawsbackend.Domain.dtos.AplicationUserDto;
import org.ncapas.happypawsbackend.services.AplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aplication")
public class AplicationController {

    @Autowired
    private AplicationService aplicationService;

    @PostMapping("/create")
    public ResponseEntity<?> createAplication(@RequestBody @Valid AplicationRegisterDto request) {
        aplicationService.createAplication(request);
        return ResponseEntity.ok("Solicitud enviada con éxito!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAplication(@PathVariable UUID id) {
        aplicationService.deleteAplication(id);
        return ResponseEntity.ok("Solicitud Eliminada con éxito!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateState(@PathVariable UUID id, @RequestBody @Valid AplicationUpdateDto request) {
        aplicationService.updateApplicationState(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<AplicationResponse>> getAllApplications() {
        List<AplicationResponse> list = aplicationService.getAllApplications();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public AplicationResponse getAplicationById(@PathVariable UUID id) {
        return aplicationService.getAplicationById(id);

    }

    @GetMapping("/by-user")
    public ResponseEntity<?> getAplicationsUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(aplicationService.getAplicationsByUser(email));
    }


    @GetMapping("/accepted")
    public ResponseEntity<List<AplicationUserDto>> getAplicationsByState() {
            List<AplicationUserDto> solicitudes = aplicationService.getAcceptedAplicationsByLoggedUser();
            return ResponseEntity.ok(solicitudes);
        }

}
