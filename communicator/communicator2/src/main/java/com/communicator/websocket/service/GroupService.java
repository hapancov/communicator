package com.communicator.websocket.service;

import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.GroupCreated;

import java.util.List;

public interface GroupService {
    void saveGroup(Group group);

    Group saveGroupAsGroupCreated(GroupCreated group);

    List<Group> findConnectedUsers();
}
