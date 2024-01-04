package com.communicator.websocket.mapper;

import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.User;
import com.communicator.websocket.model.dto.GroupDTO;
import com.communicator.websocket.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "usersId", source = "users")
    GroupDTO GroupDTO(Group group);

    default Set<String> mapUsersToUserIds(Set<User> users) {
        return users.stream()
                .map(User::getId)
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }
    List<GroupDTO> GroupDTOList(List<Group> groups);
}
