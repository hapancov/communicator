package com.communicator.websocket.service;

import com.communicator.websocket.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    void saveUser(User user);
    void disconnect(User user);
    List<User> findConnectedUsers();

    Set<User> findAllByUsername(Set<String> username);
}
