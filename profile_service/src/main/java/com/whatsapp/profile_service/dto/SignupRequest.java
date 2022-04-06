package com.whatsapp.profile_service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequest {
    @NotNull(message="username must not be null")
    private String username;
    @NotNull(message="password must not be null")
    private String password;
    @Email(message="email must be valid")
    private String email;
}
