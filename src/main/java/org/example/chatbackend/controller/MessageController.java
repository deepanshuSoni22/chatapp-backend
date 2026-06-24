package org.example.chatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatbackend.dto.response.MessageResponse;
import org.example.chatbackend.entity.User;
import org.example.chatbackend.repository.UserRepository;
import org.example.chatbackend.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @GetMapping("/{otherUserId}")
    public List<MessageResponse> getHistory(
            Authentication authentication,
            @PathVariable Integer otherUserId
    ) {

        String username = authentication.getName();

        User currentUser = userRepository
                .findUserByUsername(username)
                .orElseThrow();

        return messageService.getMessageHistory(
                currentUser.getId(),
                otherUserId
        );
    }

}
