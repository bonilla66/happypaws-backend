package org.ncapas.happypawsbackend.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ncapas.happypawsbackend.Domain.Entities.User;
import org.ncapas.happypawsbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/auth/login")
                || path.equals("/auth/register")
                || path.equals("/auth/refresh")
                || path.equals("/auth/logout")
                || path.equals("/auth/me")
                || path.startsWith("/enums/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;

        // extrar desde la cooke el token
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // filtro para buscar desde el header Authorization
        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        // si hay, validar token y setearlo
        if (token != null && jwtUtils.isTokenValid(token)) {
            System.out.println("TOKEN válido: " + jwtUtils.isTokenValid(token));
            System.out.println("USUARIO extraído: " + jwtUtils.extractEmail(token));

            String email = jwtUtils.extractEmail(token);
            userRepository.findUserByEmail(email).ifPresent(user -> {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("🟢 Usuario autenticado en el contexto: " + user.getEmail());
                System.out.println("🛡️ Autoridades: " + user.getAuthorities());
                System.out.println("🆔 ID del usuario: " + user.getId());
                System.out.println("🎭 Rol del usuario: " + user.getRol().getName().name());

                System.out.println("Usuario autenticado en el contexto");
                User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                System.out.println("👉 Clase del principal: " + authenticatedUser.getClass());
                System.out.println("Usuario autenticado: " + authenticatedUser.getEmail());
            });
        } else {
            System.out.println("Token inválido o no encontrado");
        }

        System.out.println("🚨 PASÓ JWT FILTER → va al controlador");
        filterChain.doFilter(request, response);
    }

}
