package com.communicator.websocket.repository;


import com.communicator.websocket.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, String> {
    Group findByName(String recipientId);
}
