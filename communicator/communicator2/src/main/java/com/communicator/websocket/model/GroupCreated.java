package com.communicator.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class GroupCreated {
    private String name;
    @JsonProperty("users")
    private Set<String> username = new HashSet<>();
}
