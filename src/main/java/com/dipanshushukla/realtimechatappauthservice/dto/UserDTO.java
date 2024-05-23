package com.dipanshushukla.realtimechatappauthservice.dto;

import com.dipanshushukla.realtimechatappauthservice.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    @NotNull
    @NotBlank
    private String fullName;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String email;

    private Role role = Role.USER;
}
