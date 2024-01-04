package com.communicator.websocket.mapper;

import com.communicator.websocket.model.User;
import com.communicator.websocket.model.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-04T13:29:51+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setGroupsId( mapGroupsToIds( user.getGroups() ) );
        userDTO.setId( user.getId() );
        userDTO.setNickName( user.getNickName() );
        userDTO.setFullName( user.getFullName() );
        userDTO.setStatus( user.getStatus() );

        return userDTO;
    }

    @Override
    public List<UserDTO> toUserDTOList(List<User> connectedUsers) {
        if ( connectedUsers == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( connectedUsers.size() );
        for ( User user : connectedUsers ) {
            list.add( toUserDTO( user ) );
        }

        return list;
    }
}
