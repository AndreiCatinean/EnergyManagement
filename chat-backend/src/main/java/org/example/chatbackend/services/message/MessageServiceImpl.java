package org.example.chatbackend.services.message;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.chatbackend.dto.MessageDTO;
import org.example.chatbackend.dto.ReadDTO;
import org.example.chatbackend.dto.SendMessageDTO;
import org.example.chatbackend.entities.Conversation;
import org.example.chatbackend.entities.Message;
import org.example.chatbackend.repositories.ConversationRepository;
import org.example.chatbackend.repositories.MessageRepository;
import org.example.chatbackend.repositories.UserRepository;
import org.example.chatbackend.services.conversation.ConversationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void sendMessage(SendMessageDTO messageDTO) {
        Conversation conversation = conversationRepository.findById(messageDTO.conversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Message message = Message.builder()
                .sender(userRepository.getReferenceById(messageDTO.senderId()))
                .conversation(conversation)
                .content(messageDTO.content())
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        messageRepository.save(message);

        conversation.getParticipants().forEach(user -> {
            String destination = "/queue/" + user.getId().toString();
            messagingTemplate.convertAndSend(destination, messageToDTO(message));
        });
        if (conversation.getParticipants().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/group", messageToDTO(message));
        }
    }

    @Override
    @Transactional
    public List<MessageDTO> getMessagesByConversation(UUID conversationId) {
        return toMessageDTO(messageRepository.findByConversationIdOrderByTimestamp(conversationId));
    }


    @Override
    @Transactional
    public void markMessagesAsRead(ReadDTO readDTO) {
        Conversation conversation = conversationRepository.findById(readDTO.conversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        List<Message> unreadMessages = messageRepository.findByConversationIdAndSenderIdAndReadFalse(readDTO.conversationId(), readDTO.senderId());

        for (Message message : unreadMessages) {

            message.setRead(true);
        }

        messageRepository.saveAll(unreadMessages);

        unreadMessages.forEach(message -> {
            conversation.getParticipants().forEach(user -> {
                String destination = "/queue/" + user.getId().toString();
                messagingTemplate.convertAndSend(destination, messageToDTO(message));
            });
        });
    }

    private static MessageDTO messageToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getTimestamp(),
                message.getSender().getId(),
                message.getConversation().getId(),
                message.isRead()
        );
    }

    private static List<MessageDTO> toMessageDTO(List<Message> messages) {
        return messages.stream().map(MessageServiceImpl::messageToDTO)
                .toList();
    }
}
