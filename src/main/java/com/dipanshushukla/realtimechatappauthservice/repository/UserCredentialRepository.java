package com.dipanshushukla.realtimechatappauthservice.repository;

import com.dipanshushukla.realtimechatappauthservice.entity.UserCredential;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential,Integer>{
    Optional<UserCredential> findByUsername(String username);
}
