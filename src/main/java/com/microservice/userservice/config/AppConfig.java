package com.microservice.userservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class AppConfig {

    @Value("${rsa.note-public-key-path}")
    private String notePublicKeyPath;

    @Bean
    public PublicKey noteServicePublicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(notePublicKeyPath.replace("classpath:", "src/main/resources/")));
        String publicKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
