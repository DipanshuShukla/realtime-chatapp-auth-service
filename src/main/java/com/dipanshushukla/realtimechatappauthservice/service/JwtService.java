package com.dipanshushukla.realtimechatappauthservice.service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.public-key}")
    private String publicKeyPem;

    @Value("${jwt.private-key}")
    private String privateKeyPem;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            logger.info("Initializing JwtService...");
            logger.debug("Raw Private Key: {}", privateKeyPem);
            logger.debug("Raw Public Key: {}", publicKeyPem);
            privateKey = loadPrivateKey(privateKeyPem);
            publicKey = loadPublicKey(publicKeyPem);
            logger.info("Keys successfully loaded.");
        } catch (Exception e) {
            logger.error("Failed to load keys", e);
            throw new RuntimeException("Failed to initialize JwtService", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6L * 60 * 60 * 1000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey loadPrivateKey(String pem) throws Exception {
        logger.debug("Loading private key");
        String privateKeyPem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                                  .replace("-----END PRIVATE KEY-----", "")
                                  .replaceAll("\\s", "");
        logger.debug("Processed Private Key: {}", privateKeyPem);
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String pem) throws Exception {
        logger.debug("Loading public key");
        String publicKeyPem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                                 .replace("-----END PUBLIC KEY-----", "")
                                 .replaceAll("\\s", "");
        logger.debug("Processed Public Key: {}", publicKeyPem);
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyPem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
