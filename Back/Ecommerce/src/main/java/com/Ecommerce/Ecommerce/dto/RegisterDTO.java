package com.Ecommerce.Ecommerce.dto;

import com.Ecommerce.Ecommerce.config.UserRole;

public record RegisterDTO(String nome, String login, String email, String password, UserRole role) {

}
