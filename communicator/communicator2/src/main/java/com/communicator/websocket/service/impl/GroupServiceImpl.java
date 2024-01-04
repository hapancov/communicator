package com.communicator.websocket.service.impl;

import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.GroupCreated;
import com.communicator.websocket.repository.GroupRepository;
import com.communicator.websocket.service.GroupService;
import com.communicator.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;

    @Override
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    @Override
    public Group saveGroupAsGroupCreated(GroupCreated group) {
        Group group1 = new Group();
        group1.setName(group.getName());
        System.out.println(group.getUsername());
        group1.setUsers(userService.findAllByUsername(group.getUsername()));
        groupRepository.save(group1);
        return group1;
    }

    @Override
    public List<Group> findConnectedUsers() {
        return groupRepository.findAll();
    }
}
