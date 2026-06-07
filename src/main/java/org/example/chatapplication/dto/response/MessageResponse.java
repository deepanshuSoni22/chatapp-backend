package org.example.chatapplication.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Integer id;

    private Integer senderId;

    private String senderUsername;

    private Integer receiverId;

    private String receiverUsername;

    private String content;

    private LocalDateTime timeStamp;


}
