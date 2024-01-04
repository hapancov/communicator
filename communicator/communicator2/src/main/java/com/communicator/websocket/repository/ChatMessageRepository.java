package com.communicator.websocket.repository;

import com.communicator.websocket.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);

}
