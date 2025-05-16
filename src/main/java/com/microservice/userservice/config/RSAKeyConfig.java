package com.microservice.userservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Getter
public class RSAKeyConfig {

    @Value("${rsa.note-public-key-path}")
    private String publicKeyPath;

    private PublicKey noteServicePublicKey;

    @PostConstruct
    public void loadKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(publicKeyPath.replace("classpath:", "src/main/resources/")));
        String publicKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.noteServicePublicKey = keyFactory.generatePublic(keySpec);
    }
}
