package com.communicator.websocket.service.impl;


import com.communicator.websocket.model.Status;
import com.communicator.websocket.model.User;
import com.communicator.websocket.repository.UserRepository;
import com.communicator.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);

        Optional<User> existingUser = repository.findByNickName(user.getNickName());
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setStatus(user.getStatus());
            repository.save(userToUpdate);
        } else {
            repository.save(user);
        }
    }

    @Override
    public void disconnect(User user) {
        var storedUser = repository.findByNickName(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    @Override
    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }

    @Override
    public Set<User> findAllByUsername(Set<String> usernames) {
        Set<User> users = new HashSet<>();
        for (String nick : usernames) {
            User user = repository.findByNickName(nick)
                    .orElseThrow(() -> new NoSuchElementException("User not found with nickname: " + nick));
            users.add(user);
        }

        return users;
    }

}
