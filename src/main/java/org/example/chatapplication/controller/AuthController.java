package org.example.chatapplication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.chatapplication.dto.request.UserRequest;
import org.example.chatapplication.dto.response.LoginResponse;
import org.example.chatapplication.dto.response.UserResponse;
import org.example.chatapplication.service.JwtService;
import org.example.chatapplication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody UserRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token =
                jwtService.generateToken(
                        request.getUsername()
                );

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }


}
