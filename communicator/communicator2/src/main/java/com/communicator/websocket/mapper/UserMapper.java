package com.communicator.websocket.mapper;

import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.User;
import com.communicator.websocket.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "groupsId", source = "groups")
    UserDTO toUserDTO(User user);

    default Set<String> mapGroupsToIds(Set<Group> groups) {
        return groups.stream()
                .map(Group::getId)
                .collect(Collectors.toSet());
    }

    List<UserDTO> toUserDTOList(List<User> connectedUsers);
}
