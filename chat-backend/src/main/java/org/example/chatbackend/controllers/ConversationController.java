package org.example.chatbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.chatbackend.dto.ConversationDTO;
import org.example.chatbackend.dto.MessageDTO;
import org.example.chatbackend.services.conversation.ConversationService;
import org.example.chatbackend.services.message.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversation")

public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;


    @PostMapping("/one-to-one")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ConversationDTO> createOneToOneConversation(@RequestParam UUID user1Id, @RequestParam UUID user2Id) {
        ConversationDTO conversation = conversationService.createOneToOneConversation(user1Id, user2Id);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/conversations")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(conversationService.getUserConversations(userId));
    }

    @GetMapping("/{conversationId}/messages")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<MessageDTO>> getConversationMessages(@PathVariable UUID conversationId) {
        List<MessageDTO> messages = messageService.getMessagesByConversation(conversationId);
        return ResponseEntity.ok(messages);
    }

}
