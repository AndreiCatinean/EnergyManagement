package org.example.chatbackend.services.conversation;

import org.example.chatbackend.dto.ConversationDTO;
import org.example.chatbackend.entities.Conversation;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

    Conversation getGroupChat();

    ConversationDTO createOneToOneConversation(UUID user1Id, UUID user2Id);

    List<ConversationDTO> getUserConversations(UUID userId);
}
