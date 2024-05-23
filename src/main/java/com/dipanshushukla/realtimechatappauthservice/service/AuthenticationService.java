package com.dipanshushukla.realtimechatappauthservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dipanshushukla.realtimechatappauthservice.dto.JwtResponseDTO;
import com.dipanshushukla.realtimechatappauthservice.dto.UserDTO;
import com.dipanshushukla.realtimechatappauthservice.dto.UserLoginCredentialsDTO;
import com.dipanshushukla.realtimechatappauthservice.entity.User;
import com.dipanshushukla.realtimechatappauthservice.repository.UserCredentialRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;



    public JwtResponseDTO register(UserDTO request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());
        user.setRole(user.getRole());

        user = repository.save(user);

        UserLoginCredentialsDTO userLoginCredentialsDTO = new UserLoginCredentialsDTO(user.getUsername(), user.getPassword());

        String accessToken = jwtService.generateAccessToken(userLoginCredentialsDTO.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userLoginCredentialsDTO.getUsername());

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public JwtResponseDTO authenticate(UserLoginCredentialsDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        repository.findByUsername(request.getUsername()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(request.getUsername());
        String refreshToken = jwtService.generateRefreshToken(request.getUsername());

        return new JwtResponseDTO(accessToken, refreshToken);
    }
}
