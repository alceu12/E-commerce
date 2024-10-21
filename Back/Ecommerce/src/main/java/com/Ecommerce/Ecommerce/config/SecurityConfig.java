package com.Ecommerce.Ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors() // Ativa o CORS definido no WebConfig
            .and()
            .csrf().disable() // Desativa CSRF para facilitar o desenvolvimento
            .authorizeRequests()
            .anyRequest().permitAll() // Permite todas as requisições sem autenticação
            .and()
            .httpBasic().disable(); // Desativa a autenticação básica

        return http.build();
    }
}
