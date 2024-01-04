package com.communicator.websocket.controller;

import com.communicator.websocket.mapper.GroupMapper;
import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.GroupCreated;
import com.communicator.websocket.model.User;
import com.communicator.websocket.model.dto.GroupDTO;
import com.communicator.websocket.service.GroupService;
import com.communicator.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @MessageMapping("/group.createGroup")
    @SendTo("/user/public")
    public GroupDTO createGroup(@Payload GroupCreated group) {
        return groupMapper.GroupDTO(groupService.saveGroupAsGroupCreated(group)) ;
    }

    @GetMapping("/group")
    public ResponseEntity<List<GroupDTO>> findConnectedUsers() {
        System.out.println(21);
        return ResponseEntity.ok(groupMapper.GroupDTOList(groupService.findConnectedUsers()));
    }
}
