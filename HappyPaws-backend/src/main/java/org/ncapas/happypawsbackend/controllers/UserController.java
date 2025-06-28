package org.ncapas.happypawsbackend.controllers;

import jakarta.validation.Valid;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.Domain.Enums.UserRol;
import org.ncapas.happypawsbackend.Domain.dtos.UserDto;
import org.ncapas.happypawsbackend.Domain.dtos.UserDto2;
import org.ncapas.happypawsbackend.services.UserService;
import org.ncapas.happypawsbackend.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceimpl;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADOPTANTE', 'COLABORADOR')")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User authUser = userServiceimpl.getUserByEmailEntity(email);

        boolean isAdmin = authUser.getRol().getName() == UserRol.ADMIN;

        if (!isAdmin && !authUser.getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            System.out.println(">>> LLAMADO A DELETE con ID: " + id);
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            System.out.println(">>> ERROR AL ELIMINAR: " + errorMessage);

            if (errorMessage != null && errorMessage.contains("relaciones activas")) {
                return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
            }

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", errorMessage != null ? errorMessage : "Error desconocido al eliminar usuario"));
        }
    }



    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR', 'ADOPTANTE')")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserDto2 updatedUser, Authentication authentication) {
        System.out.println("🔥 ENTRÓ AL MÉTODO PATCH");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User authUser = userServiceimpl.getUserByEmailEntity(email);

        boolean isAdmin = UserRol.ADMIN.equals(authUser.getRol().getName());

        if (!isAdmin && !authUser.getId().equals(id)) {
            return ResponseEntity.status(403).body(Map.of("error", "No tiene permiso para modificar otro perfil."));
        }

        try {
            UserDto updated = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADOPTANTE', 'COLABORADOR')")
    public ResponseEntity<?> getUserByEmail(@RequestParam @Valid String email, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentEmail = userDetails.getUsername();
        User authUser = userServiceimpl.getUserByEmailEntity(currentEmail);


        if (!authUser.getRol().getName().equals(UserRol.ADMIN) &&
                !authUser.getRol().getName().equals(UserRol.COLABORADOR) &&
                !authUser.getEmail().equals(email)) {
            return ResponseEntity.status(403).body("No esta autorizado para consultar este correo");
        }

        try {
            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @GetMapping("/testauth")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR', 'ADOPTANTE')")
    public ResponseEntity<String> testAuth() {
        System.out.println("✅ ENTRÓ AL CONTROLADOR /testauth");
        return ResponseEntity.ok("Entró correctamente al controlador protegido");
    }



}
