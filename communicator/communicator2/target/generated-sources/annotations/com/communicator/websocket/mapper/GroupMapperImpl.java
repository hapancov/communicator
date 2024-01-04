package com.communicator.websocket.mapper;

import com.communicator.websocket.model.Group;
import com.communicator.websocket.model.dto.GroupDTO;
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
public class GroupMapperImpl implements GroupMapper {

    @Override
    public GroupDTO GroupDTO(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupDTO groupDTO = new GroupDTO();

        groupDTO.setUsersId( mapUsersToUserIds( group.getUsers() ) );
        if ( group.getId() != null ) {
            groupDTO.setId( Long.parseLong( group.getId() ) );
        }
        groupDTO.setName( group.getName() );

        return groupDTO;
    }

    @Override
    public List<GroupDTO> GroupDTOList(List<Group> groups) {
        if ( groups == null ) {
            return null;
        }

        List<GroupDTO> list = new ArrayList<GroupDTO>( groups.size() );
        for ( Group group : groups ) {
            list.add( GroupDTO( group ) );
        }

        return list;
    }
}
