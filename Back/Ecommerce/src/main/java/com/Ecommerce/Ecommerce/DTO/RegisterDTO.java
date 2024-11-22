package com.Ecommerce.Ecommerce.dto;

import com.Ecommerce.Ecommerce.config.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
