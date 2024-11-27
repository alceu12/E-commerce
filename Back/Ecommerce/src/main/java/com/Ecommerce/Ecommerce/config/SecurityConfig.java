package com.Ecommerce.Ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // Importante
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Uso do cors(Customizer)
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }*/

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configurações de CORS
        configuration.setAllowedOrigins(Arrays.asList("https://e-commerce-1-kuuf.onrender.com/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Se precisar permitir credenciais (cookies, autenticação HTTP)

        // Aplicar as configurações para todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/produtos/categoria").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias").permitAll()
                        .requestMatchers(HttpMethod.GET,"/produtos/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/categorias/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/carrinho").permitAll()
                        .requestMatchers(HttpMethod.POST,"/categorias").permitAll()
                        .requestMatchers(HttpMethod.GET,"/cupons/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/pedidos/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/enderecos/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/usuarios/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/pedidos/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/enderecos/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/usuarios/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/carrinho").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/carrinho/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/carrinho/limpar").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/carrinho/remover-produto/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/carrinho/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/cupons/validar/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/pedidos").permitAll()
                        .requestMatchers(HttpMethod.POST,"/cupons/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/produtos/*").hasRole("ADMIN")


                        .anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
