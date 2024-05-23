package com.dipanshushukla.realtimechatappauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginCredentialsDTO {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;
}
