package org.example.chatapplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.chatapplication.dto.request.UserRequest;
import org.example.chatapplication.dto.response.UserResponse;
import org.example.chatapplication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        System.out.println("REGISTER CONTROLLER HIT");

        UserResponse response = userService.register(request);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/me")
    public String currentUser(Authentication authentication) {
        return authentication.getName();
    }

}
