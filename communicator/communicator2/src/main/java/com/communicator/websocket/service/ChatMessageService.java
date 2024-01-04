package com.communicator.websocket.service;

import com.communicator.websocket.model.ChatMessage;
import com.communicator.websocket.model.GroupChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage save(ChatMessage chatMessage);
    GroupChatMessage saveForGroup(GroupChatMessage chatMessage);
    List<ChatMessage> findChatMessages(String senderId, String recipientId);

    List<GroupChatMessage> findGroupChatMessages(String senderId, String selectedGroupId);

}
