package org.example.chatapplication.repository;

import org.example.chatapplication.entity.Message;
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