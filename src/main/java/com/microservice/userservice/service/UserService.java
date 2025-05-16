package com.microservice.userservice.service;

import com.microservice.userservice.model.User;
import com.microservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(String email, String password) {
        Optional<User> exists = userRepository.findByEmail(email);
        if (exists.isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
