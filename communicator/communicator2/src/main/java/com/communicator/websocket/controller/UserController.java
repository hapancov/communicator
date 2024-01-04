package com.communicator.websocket.controller;

import com.communicator.websocket.mapper.UserMapper;
import com.communicator.websocket.model.User;

import com.communicator.websocket.model.dto.UserDTO;
import com.communicator.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
private final UserMapper userMapper;
    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public UserDTO addUser(
            @Payload User user
    ) {
        userService.saveUser(user);
        return userMapper.toUserDTO(user) ;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findConnectedUsers() {
        return ResponseEntity.ok(userMapper.toUserDTOList(userService.findConnectedUsers()) );
    }
}
