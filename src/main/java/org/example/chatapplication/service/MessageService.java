package org.example.chatapplication.service;

import lombok.RequiredArgsConstructor;
import org.example.chatapplication.dto.request.MessageRequest;
import org.example.chatapplication.dto.response.MessageResponse;
import org.example.chatapplication.entity.Message;
import org.example.chatapplication.entity.User;
import org.example.chatapplication.mapper.MessageMapper;
import org.example.chatapplication.repository.MessageRepository;
import org.example.chatapplication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public MessageResponse saveMessage(String senderUsername, MessageRequest request) {

        User sender = userRepository.findUserByUsername(senderUsername)
                .orElseThrow();

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow();

        Message message = new Message();

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setTimeStamp(LocalDateTime.now());

        Message saved = messageRepository.save(message);

        return messageMapper.toDto(saved);

    }

    public List<MessageResponse> getMessageHistory(Integer user1, Integer user2) {

        return messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimeStampAsc(
                        user1,
                        user2,
                        user2,
                        user1
                )
                .stream()
                .map(messageMapper::toDto)
                .toList();

    }

}
