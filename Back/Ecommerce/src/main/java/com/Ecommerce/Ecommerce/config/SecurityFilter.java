// src/main/java/com/Ecommerce/Ecommerce/config/SecurityFilter.java

package com.Ecommerce.Ecommerce.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.service.TokenService;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);
        if (token != null) {
            String subject = tokenService.validateToken(token);
            if (subject != null) {
                DecodedJWT decodedJWT = tokenService.getClaims(token);
                if (decodedJWT != null) {
                    Long userId = decodedJWT.getClaim("id").asLong();
                    String email = decodedJWT.getClaim("email").asString();
                    String role = decodedJWT.getClaim("role").asString();

                    Usuario usuario = usuarioRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(role != null ? role : "ROLE_USER"));
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do header Authorization.
     *
     * @param request A requisição HTTP.
     * @return O token JWT, ou null se não estiver presente.
     */
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}
