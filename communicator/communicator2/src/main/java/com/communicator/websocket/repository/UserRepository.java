package com.communicator.websocket.repository;

import com.communicator.websocket.model.Status;
import com.communicator.websocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, String> {
    List<User> findAllByStatus(Status status);

    Optional<User> findByNickName(String nickName);
}
