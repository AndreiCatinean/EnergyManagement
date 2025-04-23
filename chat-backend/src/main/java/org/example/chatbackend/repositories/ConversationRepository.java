package org.example.chatbackend.repositories;

import org.example.chatbackend.entities.AppUser;
import org.example.chatbackend.entities.Conversation;
import org.example.chatbackend.entities.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    Optional<Conversation> findByType(ConversationType conversationType);

    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id IN :userIds GROUP BY c HAVING COUNT(p) = :size")
    Optional<Conversation> findByParticipants(@Param("userIds") Set<UUID> userIds, @Param("size") Long size);

    Set<Conversation> findConversationsByParticipantsContaining(AppUser user);
}
