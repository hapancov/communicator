package com.communicator.websocket.service.impl;

import com.communicator.websocket.model.ChatRoom;
import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.User;
import com.communicator.websocket.repository.ChatRoomRepository;
import com.communicator.websocket.repository.GroupRepository;
import com.communicator.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final GroupRepository groupRepository;

    @Override
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        if (checkPattern(recipientId)) {
            return chatRoomRepository
                    .findBySenderIdAndRecipientId(senderId, recipientId)
                    .map(ChatRoom::getChatId)
                    .or(() -> {
                        if (createNewRoomIfNotExists) {
                            var chatId = createChatIdForGroup(senderId, recipientId);
                            return Optional.of(chatId);
                        }

                        return Optional.empty();
                    });
        } else {
            return chatRoomRepository
                    .findBySenderIdAndRecipientId(senderId, recipientId)
                    .map(ChatRoom::getChatId)
                    .or(() -> {
                        if (createNewRoomIfNotExists) {
                            var chatId = createChatId(senderId, recipientId);
                            return Optional.of(chatId);
                        }

                        return Optional.empty();
                    });

        }
    }


    @Override
    public String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

    private String createChatIdForGroup(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        chatRoomRepository.save(senderRecipient);

        Group group = groupRepository.findByName(recipientId.replaceAll("group", ""));
        Set<User> users = group.getUsers();
        for (User u : users) {
            if (!senderId.equals(u.getNickName())) {
                chatId = String.format("%s_%s", u.getNickName(), recipientId);
                ChatRoom senderRecipient2 = ChatRoom
                        .builder()
                        .chatId(chatId)
                        .senderId(u.getNickName())
                        .recipientId(recipientId)
                        .build();
                chatRoomRepository.save(senderRecipient2);
            }
        }
        return chatId;
    }


    private static boolean checkPattern(String recipientId) {
        String regex = "^group.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(recipientId);
        return matcher.matches();
    }

}
