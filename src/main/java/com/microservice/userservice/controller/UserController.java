package com.microservice.userservice.controller;

import com.microservice.userservice.dto.NoteRequest;
import com.microservice.userservice.dto.RegisterRequest;
import com.microservice.userservice.model.User;
import com.microservice.userservice.service.NoteClientService;
import com.microservice.userservice.service.S3Service;
import com.microservice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final NoteClientService noteClientService;
    private final S3Service s3Service;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/notes")
    public String saveNote(@RequestBody NoteRequest request) throws Exception {
        userService.findByEmail(request.getUserEmail());

        noteClientService.sendEncryptedNote(request.getTitle(), request.getContent(), request.getUserEmail());
        return "Note saved successfully";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file, @RequestPart String username) {
        String response = s3Service.uploadFile(file, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{username}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String username) {
        String url = s3Service.getPublicFileUrl(username);
        return ResponseEntity.ok(url);
    }

}
