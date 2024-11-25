package com.Ecommerce.Ecommerce.controller;

import java.util.Optional;

import com.Ecommerce.Ecommerce.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.Ecommerce.dto.AuthenticationDTO;
import com.Ecommerce.Ecommerce.dto.ChangeEmailDTO;
import com.Ecommerce.Ecommerce.dto.ChangePasswordDTO;
import com.Ecommerce.Ecommerce.dto.EmailDTO;
import com.Ecommerce.Ecommerce.dto.RegisterDTO;
import com.Ecommerce.Ecommerce.dto.ResetPasswordDTO;
import com.Ecommerce.Ecommerce.dto.ResponseDTO;
import com.Ecommerce.Ecommerce.dto.VerificationDTO;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.service.EmailService;
import com.Ecommerce.Ecommerce.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    //@Autowired
    //private AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    /*@SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticationDTO body){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        @SuppressWarnings("unused")
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDTO data){
        if(this.usuarioRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());

        this.usuarioRepository.save(newUser);

        return ResponseEntity.ok().build();

    }*/

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO body) {
        Usuario usuario = this.usuarioRepository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.password(), usuario.getPassword())) {
            String token = this.tokenService.generateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO body) {
        Optional<Usuario> usuario = this.usuarioRepository.findByEmail(body.email());

        if (usuario.isEmpty()) {
            Usuario newUser = new Usuario();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setNome(body.nome());
            newUser.setLogin(body.login());
            newUser.setRole(body.role());
            this.usuarioRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Usuario usuario = usuarioRepository.findByEmail(changePasswordDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), usuario.getPassword())) {
            return ResponseEntity.badRequest().body(null); // Senha antiga incorreta
        }

        usuario.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        usuarioRepository.save(usuario);

        // Invalida o token antigo e gera um novo token
        String token = tokenService.generateToken(usuario);

        // Retorna o novo token no header da resposta
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @PutMapping("/change-email")
    public ResponseEntity<Void> changeEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
        Usuario usuario = usuarioRepository.findByEmail(changeEmailDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        usuario.setEmail(changeEmailDTO.getEmail());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody EmailDTO emailDTO) {
        String email = emailDTO.getEmail();
        boolean exists = usuarioService.existsByEmail(email);
        if (exists) {
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build(); // Retorna 404 se o email não existir
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestBody VerificationDTO verificationDTO) {
        if (emailService.verifyCode(verificationDTO.getEmail(), verificationDTO.getCode())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        if (emailService.verifyCode(resetPasswordDTO.getEmail(), resetPasswordDTO.getCode())) {
            usuarioService.updatePassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getNewPassword());
            emailService.removeVerificationCode(resetPasswordDTO.getEmail());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
