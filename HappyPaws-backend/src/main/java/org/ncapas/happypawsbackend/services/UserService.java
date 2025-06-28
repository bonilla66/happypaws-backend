package org.ncapas.happypawsbackend.services;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.ncapas.happypawsbackend.Domain.Entities.RefreshToken;
import org.ncapas.happypawsbackend.Domain.Entities.Rol;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.Domain.Enums.UserRol;
import org.ncapas.happypawsbackend.Domain.dtos.UserDto;
import org.ncapas.happypawsbackend.Domain.dtos.UserDto2;
import org.ncapas.happypawsbackend.repositories.RefreshTokenRepository;
import org.ncapas.happypawsbackend.repositories.RoleRepository;
import org.ncapas.happypawsbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Optional<User> findUserByEmail(String email) {

        return userRepository.findUserByEmail(email);
    }

    public boolean existByDui(String dui) {
        return userRepository.existsByDUI(dui);
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> request = new ArrayList<>();
        for (User u : users) {
            UserDto user = new UserDto();
            user.setId_user(u.getId());
            user.setName(u.getName());
            user.setEmail(u.getEmail());
            user.setDUI(u.getDUI());
            user.setPhone(u.getPhone());
            user.setRol(u.getRol().getName().name());
            request.add(user);
        }
        return request;
    }

    public Optional<UserDto> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId_user(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setDUI(user.getDUI());
                    dto.setPhone(user.getPhone());
                    dto.setRol(user.getRol().getName().name());
                    return dto;
                });
    }

    @Transactional
    public void deleteUser(UUID id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

            System.out.println(">>> Usuario encontrado: " + user.getEmail());

            refreshTokenRepository.deleteByUser(user);
            System.out.println(">>> Tokens eliminados");

            userRepository.delete(user);
            System.out.println(">>> Usuario eliminado");

        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                System.out.println(">>> Excepción por restricción FK: relaciones activas");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el usuario porque tiene relaciones activas.");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error de integridad de datos al eliminar el usuario.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>> Tipo de excepción: " + e.getClass().getName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado al eliminar el usuario.");
        }
    }



    public UserDto updateUser(UUID id, UserDto2 updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (updatedUser.getDUI() != null && !user.getDUI().equals(updatedUser.getDUI())) {
            throw new IllegalArgumentException("No está permitido modificar el campo 'dui'");
        }

        boolean huboCambios = false;

        if (updatedUser.getName() != null && !updatedUser.getName().equals(user.getName())) {
            user.setName(updatedUser.getName());
            huboCambios = true;
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            user.setEmail(updatedUser.getEmail());
            huboCambios = true;
        }

        if (updatedUser.getPhone() != null && !updatedUser.getPhone().equals(user.getPhone())) {
            user.setPhone(updatedUser.getPhone());
            huboCambios = true;
        }

        if (updatedUser.getRol() != null &&
                !user.getRol().getName().name().equalsIgnoreCase(updatedUser.getRol())) {

            UserRol nuevoRolEnum = UserRol.valueOf(updatedUser.getRol().toUpperCase());
            Rol nuevoRol = roleRepository.findRolByName(nuevoRolEnum)
                    .orElseThrow(() -> new RuntimeException("Rol no válido: " + updatedUser.getRol()));
            user.setRol(nuevoRol);
            huboCambios = true;
        }

        if (!huboCambios) {
            throw new IllegalArgumentException("No se realizaron cambios en el perfil");
        }

        userRepository.save(user);

        UserDto dto = new UserDto();
        dto.setId_user(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDUI(user.getDUI());
        dto.setPhone(user.getPhone());
        dto.setRol(user.getRol().getName().name());

        return dto;
    }



    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId_user(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setDUI(user.getDUI());
                    dto.setPhone(user.getPhone());
                    dto.setRol(user.getRol().getName().name());
                    return dto;
                });

    }
}

