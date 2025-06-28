package org.ncapas.happypawsbackend.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.ncapas.happypawsbackend.Domain.Entities.Rol;
import org.ncapas.happypawsbackend.Domain.Entities.AccessToken;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.Domain.Enums.UserRol;
import org.ncapas.happypawsbackend.Domain.dtos.LoginDto;
import org.ncapas.happypawsbackend.Domain.dtos.RegisterDto;
import org.ncapas.happypawsbackend.repositories.RoleRepository;
import org.ncapas.happypawsbackend.repositories.AccessTokenRepository;
import org.ncapas.happypawsbackend.repositories.UserRepository;
import org.ncapas.happypawsbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EntityManager entityManager;

    public void register(RegisterDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(String.valueOf(request.getPhone()));
        user.setDUI(String.valueOf(request.getDui()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Rol rolUser = roleRepository.findRolByName(UserRol.ADOPTANTE)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRol(rolUser);
        user.setState(1);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está en uso");
        }
        if (userRepository.existsByDUI(request.getDui())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DUI ya está registrado");
        }


        userRepository.save(user);
    }
    /* var jwtToken = jwtUtils.generateToken(user);

        Token token = new Token();
        token.setToken(jwtToken);
        token.setUser(user);
        token.setRevoked(false);
        token.setExpired(false);
        tokenRepository.save(token);

      //Falta guardar el token, solo si es register y login automatiico?

    }*/

    @Transactional
    public String loginAndSaveToken(LoginDto request) {
        User user = getValidUser(request);

        String jwt = jwtUtils.generateToken(user);

        AccessToken accessToken = AccessToken.builder()
                .token(jwt)
                .user(user)
                .tokenType(AccessToken.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        accessTokenRepository.save(accessToken);

        return jwt;
    }

    public User getValidUser(LoginDto request) {
        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        if (user.getState() == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este usuario está deshabilitado");
        }

        return user;
    }

}


