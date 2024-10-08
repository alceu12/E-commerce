package com.Ecommerce.Ecommerce.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Ecommerce.Ecommerce.dto.UsuarioDTO;

public class ValidaEmailTest {
    String emailValido = "mateus@senac.com.br";
    String emailInvalido = "mateussenac.com.br";
    String emailNulo = null;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp(){
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("mateus@senac.com.br");
    }

    @Test
    void caracterArroba(){   

    assertTrue(ValidaEmail.validarCaracterArroba(emailValido), "O email válido deve conter '@'" );
    assertFalse(ValidaEmail.validarCaracterArroba(emailInvalido), "O email inválido não deve conter '@'");
    assertFalse(ValidaEmail.validarCaracterArroba(emailNulo), "O email nulo não deve conter nenhum caracter");
    }

    @Test
    void ConfirmarEmail(){
        String emailAntigo = "mateus@senac.com.br";

        //Verifica se o valor esperado é igual ao valor atual.
        //Verifica se os dois são iguais
        //Passa no teste se for igual
        assertEquals(emailValido, usuarioDTO.getEmail(),"Os emails devem ser iguais");

        //Verifica se o valor atual não é igual ao valor esperado.
        //Verifica se os emails são diferentes
        //Passa no teste se for diferente
        assertEquals(usuarioDTO.getEmail(),emailAntigo,"Os emails devem ser iguais");

        //Verifica se o objeto é nulo
        //Verifica se a variavel é nula
        //passa se for nulo
        assertNull(emailNulo);

        //Verifica se o objeto não é nulo
        //Verifica se o objeto usuario não é nulo
        //passa se o usuario não for nulo
        assertNotNull(usuarioDTO);
        
        //Permite agrupar multiplas asserções, executando todas elas e reportando todas as falhas.
        //Verifica se os emails são iguais e validos
        //Passa se os emails forem iguais e validos
        assertAll("Valida Email",
            () -> assertEquals(emailValido, usuarioDTO.getEmail(),"Os emails devem ser iguais"),
            () -> assertEquals(usuarioDTO.getEmail(),emailAntigo,"Os emails devem ser iguais")
        );
    }

    @Test
    void testEmailValido() {
        assertTrue(ValidaEmail.isValid(usuarioDTO.getEmail()));
    }

    @Test
    void testEmailInvalidoSemArroba() {
        assertFalse(ValidaEmail.isValid(emailInvalido));
    }

    @Test
    void testEmailInvalidoDominio() {
        assertFalse(ValidaEmail.isValid("mateus@.com"));
    }

    @Test
    void testEmailInvalidoTLD() {
        //Testando um email sem TLD (top level domain) - (ex: .com, .org)
        assertFalse(ValidaEmail.isValid("mateus@senac"));
    }

    @Test
    void testEmailInvalidoCaracterEspecial() {
        assertFalse(ValidaEmail.isValid("mateus@senac!aluno.com"));
    }

    @Test
    void testEmailValidoSubDominio() {
        assertTrue(ValidaEmail.isValid("mateus@aluno.sc.senac.br"));
    }

    @Test
    void testEmailValidoNumeroDominio() {
        assertTrue(ValidaEmail.isValid("mateus@123.com"));
    }

    @Test
    void testEmailInvalidoPontos() {
        assertFalse(ValidaEmail.isValid("mateus..senac@aluno.com"));
    }
}
