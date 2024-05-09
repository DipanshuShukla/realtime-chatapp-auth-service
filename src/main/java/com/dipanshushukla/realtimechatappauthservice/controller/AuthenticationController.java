package com.dipanshushukla.realtimechatappauthservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.dipanshushukla.realtimechatappauthservice.entity.UserCredential;
import com.dipanshushukla.realtimechatappauthservice.model.AuthenticationResponse;
import com.dipanshushukla.realtimechatappauthservice.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

@PostMapping("/register")
public ResponseEntity<AuthenticationResponse> register(@RequestBody UserCredential request) {
    
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
}

@PostMapping("/login")
public ResponseEntity<AuthenticationResponse> login(@RequestBody UserCredential request) {
    
    return ResponseEntity.ok(authService.authenticate(request));
}


}
