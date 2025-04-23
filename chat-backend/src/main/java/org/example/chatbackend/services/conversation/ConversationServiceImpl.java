package org.example.chatbackend.services.conversation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.chatbackend.dto.ConversationDTO;
import org.example.chatbackend.dto.UserConversationDTO;
import org.example.chatbackend.entities.AppUser;
import org.example.chatbackend.entities.Conversation;
import org.example.chatbackend.entities.ConversationType;
import org.example.chatbackend.repositories.ConversationRepository;
import org.example.chatbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Conversation getGroupChat() {
        return conversationRepository.findByType(ConversationType.GROUP_CHAT).orElseGet(() -> {
            Conversation groupChat = Conversation.builder()
                    .type(ConversationType.GROUP_CHAT)
                    .participants(Set.of())
                    .build();
            return conversationRepository.save(groupChat);
        });
    }

    @Transactional
    public ConversationDTO createOneToOneConversation(UUID user1Id, UUID user2Id) {
        AppUser user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User1 not found"));

        AppUser user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        Set<UUID> userIds = Set.of(user1.getId(), user2.getId());

        Conversation conversation = conversationRepository.findByParticipants(userIds, (long) userIds.size())
                .orElseGet(() -> {
                    Conversation newConversation = Conversation.builder()
                            .type(ConversationType.ONE_TO_ONE_CHAT)
                            .participants(Set.of(user1, user2))
                            .build();
                    return conversationRepository.save(newConversation);
                });

        List<UserConversationDTO> participantDTOs = conversation.getParticipants().stream()
                .map(participant -> new UserConversationDTO(participant.getId(), participant.getUsername()))
                .collect(Collectors.toList());

        return new ConversationDTO(conversation.getId(), participantDTOs);
    }

    @Override
    @Transactional
    public List<ConversationDTO> getUserConversations(UUID userId) {

        AppUser appUser = userRepository.findById(userId).orElseThrow();

        Set<Conversation> conversations = conversationRepository.findConversationsByParticipantsContaining(appUser);

        conversations.add(getGroupChat());

        return conversations.stream()
                .map(conversation -> {
                    List<UserConversationDTO> participants = conversation.getParticipants().stream().map(appUser1 ->
                            new UserConversationDTO(appUser1.getId(), appUser1.getUsername())
                    ).collect(Collectors.toList());
                    return new ConversationDTO(conversation.getId(), participants);
                })
                .collect(Collectors.toList());
    }
}
