package org.ncapas.happypawsbackend.utils;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class JwtUtils {

    // se carga desde application.properties
    @Value("${jwt.secret}")
    private String jwtSecret; //Crear variable de la secret key

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // genera un JWT con roles
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    // crear el JWT con fecha de creacion y expiracion
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSecretKey())
                .compact();
    }

    // extrae el email desde el token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return claims.get("roles", List.class);
    }

    // verifica que el token no haya expirado y sea el del usuario
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // verifica fecha de expiracion del token
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }

    // valida el token
    public boolean isTokenValid(String token) {
        try {
            token = token.replace("Bearer ", "").trim();
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (DecodingException e) {
            System.err.println("Error de decodificación: " + e.getMessage());
            System.err.println("Token recibido: " + token);
        } catch (JwtException e) {
            System.err.println("Error JWT: " + e.getMessage());
        }
        return false;
    }

    // genera token de refresh
    public String generateRefreshToken(User user){
        return java.util.UUID.randomUUID().toString(); // lo gaurda en la bd
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject(); //
    }

}