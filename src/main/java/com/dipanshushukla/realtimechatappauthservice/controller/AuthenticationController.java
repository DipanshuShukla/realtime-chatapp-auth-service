package com.dipanshushukla.realtimechatappauthservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.dipanshushukla.realtimechatappauthservice.dto.JwtResponseDTO;
import com.dipanshushukla.realtimechatappauthservice.dto.UserDTO;
import com.dipanshushukla.realtimechatappauthservice.dto.UserLoginCredentialsDTO;
import com.dipanshushukla.realtimechatappauthservice.service.AuthenticationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@Valid @RequestBody UserDTO request) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody UserLoginCredentialsDTO request) {
        
        return ResponseEntity.ok(authService.authenticate(request));
    }


}
