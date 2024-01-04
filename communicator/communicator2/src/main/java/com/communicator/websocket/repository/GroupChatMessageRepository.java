package com.communicator.websocket.repository;

import com.communicator.websocket.model.ChatMessage;
import com.communicator.websocket.model.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, String> {
    @Query(value = "SELECT * FROM group_chat_message WHERE chat_id LIKE %:suffix", nativeQuery = true)
    List<GroupChatMessage> findAllByChatIdEndingWith(@Param("suffix") String suffix);

  }
