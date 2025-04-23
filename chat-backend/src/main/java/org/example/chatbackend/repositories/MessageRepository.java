package org.example.chatbackend.repositories;

import org.example.chatbackend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderByTimestamp(UUID conversationId);

    List<Message> findByConversationIdAndSenderIdAndReadFalse(UUID conversationId, UUID senderId);
}
