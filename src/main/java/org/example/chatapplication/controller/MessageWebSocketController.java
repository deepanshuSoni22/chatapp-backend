package org.example.chatapplication.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatapplication.dto.request.MessageRequest;
import org.example.chatapplication.dto.response.MessageResponse;
import org.example.chatapplication.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MessageWebSocketController.class);

    @MessageMapping("/chat.send")
    public void sendMessage(MessageRequest request, Principal principal) {
        logger.info("[WebSocket] Message received from principal: {}", principal != null ? principal.getName() : "NULL");

        if (principal == null) {
            logger.error("[WebSocket] Principal is NULL! STOMP session not authenticated");
            return;
        }

        MessageResponse response = messageService.saveMessage(principal.getName(), request);
        logger.info("[WebSocket] Message saved. Receiver: {}, Sender: {}",
                response.getReceiverUsername(), response.getSenderUsername());

        // Send to receiver
        logger.info("[WebSocket] Sending to receiver '{}' via convertAndSendToUser", response.getReceiverUsername());
        messagingTemplate.convertAndSendToUser(
                response.getReceiverUsername(),
                "/queue/messages",
                response
        );

        // Send to sender (optional, for confirmation)
        logger.info("[WebSocket] Sending to sender '{}' via convertAndSendToUser", response.getSenderUsername());
        messagingTemplate.convertAndSendToUser(
                response.getSenderUsername(),
                "/queue/messages",
                response
        );
    }
}