package com.communicator.websocket.model.dto;

import java.util.HashSet;
import java.util.Set;

public class GroupDTO {
    private Long id;
    private String name;
    private Set<String> usersId = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getUsersId() {
        return usersId;
    }

    public void setUsersId(Set<String> usersId) {
        this.usersId = usersId;
    }
}
