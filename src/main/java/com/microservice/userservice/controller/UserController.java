package com.microservice.userservice.controller;

import com.microservice.userservice.dto.NoteRequest;
import com.microservice.userservice.dto.RegisterRequest;
import com.microservice.userservice.model.User;
import com.microservice.userservice.service.NoteClientService;
import com.microservice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final NoteClientService noteClientService;

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

}
