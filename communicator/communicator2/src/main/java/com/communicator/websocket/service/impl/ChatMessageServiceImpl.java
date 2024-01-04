package com.communicator.websocket.service.impl;

import com.communicator.websocket.model.ChatMessage;
import com.communicator.websocket.model.GroupChatMessage;
import com.communicator.websocket.repository.ChatMessageRepository;
import com.communicator.websocket.repository.GroupChatMessageRepository;
import com.communicator.websocket.service.ChatMessageService;
import com.communicator.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository userChatRepository;
    private final ChatRoomService chatRoomService;
    private final GroupChatMessageRepository groupChatRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        userChatRepository.save(chatMessage);
        return chatMessage;
    }
    @Override
    public GroupChatMessage saveForGroup(GroupChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(),"group" +  chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        groupChatRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        System.out.println(chatId);
        return chatId.map(userChatRepository::findByChatId).orElse(new ArrayList<>());
    }

    @Override
    public List<GroupChatMessage> findGroupChatMessages(String senderId, String selectedGroupId) {
        selectedGroupId= "group" + selectedGroupId;
        var chatId =  chatRoomService.getChatRoomId(senderId, selectedGroupId, false);
        String a = getChatIdSuffix(chatId.orElse("1_0"));
        List<GroupChatMessage> messages = groupChatRepository.findAllByChatIdEndingWith(a);
        return groupChatRepository.findAllByChatIdEndingWith(a);
    }


    public String getChatIdSuffix(String chatId) {
        String[] parts = chatId.split("_");
        if (parts.length == 2) {
            return  parts[1];
        } else {
            throw new IllegalArgumentException("Invalid chatId format: " + chatId);
        }
    }

}
