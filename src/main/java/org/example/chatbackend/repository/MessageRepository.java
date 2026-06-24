package org.example.chatbackend.repository;

import org.example.chatbackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimeStampAsc(
            Integer sender1,
            Integer receiver1,
            Integer sender2,
            Integer receiver2
    );

}