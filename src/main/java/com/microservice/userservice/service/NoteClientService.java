package com.microservice.userservice.service;


import com.microservice.userservice.config.RSAKeyConfig;
import com.microservice.userservice.utils.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoteClientService {

    private final RestTemplate restTemplate;

    private final RSAKeyConfig rsaKeyConfig;

    @Value("${note-service.url}")
    private String noteServiceUrl;

    public void sendEncryptedNote(String title, String content, String userEmail) throws Exception {

        SecretKey aesKey = CryptoUtils.generateAESKey();
        String encryptedContent = CryptoUtils.encryptWithAES(content, aesKey);


        PublicKey noteServicePublicKey = rsaKeyConfig.getNoteServicePublicKey();
        String encryptedAESKey = CryptoUtils.encryptAESKeyWithRSA(aesKey, noteServicePublicKey);


        Map<String, String> payload = Map.of(
                "title", title,
                "encryptedContent", encryptedContent,
                "encryptedAESKey", encryptedAESKey,
                "userEmail", userEmail
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        restTemplate.postForEntity(noteServiceUrl, request, String.class);
    }
}
