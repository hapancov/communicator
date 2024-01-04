package com.communicator.websocket.controller;

import com.communicator.websocket.model.*;

import com.communicator.websocket.repository.GroupRepository;
import com.communicator.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final GroupRepository groupRepository;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }
    @MessageMapping("/group/chat")
    public void processMessageForGroup(@Payload GroupChatMessage chatMessage) {
        GroupChatMessage savedMsg = chatMessageService.saveForGroup(chatMessage);
        Group group = groupRepository.findByName(chatMessage.getRecipientId().replaceAll("group", ""));
        Set<User> users = group.getUsers();
        for (User user : users) {
            messagingTemplate.convertAndSendToUser(
                    user.getNickName(), "/queue/messages",
                    new ChatNotification(
                            savedMsg.getId(),
                            savedMsg.getSenderId(),
                            savedMsg.getRecipientId(),
                            savedMsg.getContent()
                    )
            );
        }
    }
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
    @GetMapping("/group/messages/{senderId}/{selectedGroupId}")
    public ResponseEntity<List<GroupChatMessage>> findGroupChatMessages(@PathVariable String senderId,@PathVariable String selectedGroupId) {
        return ResponseEntity
                .ok(chatMessageService.findGroupChatMessages(senderId, selectedGroupId));
    }
}
