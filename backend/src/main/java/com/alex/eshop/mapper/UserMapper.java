package com.alex.eshop.mapper;

import com.alex.eshop.dto.userDTOs.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;



@Mapper
public interface UserMapper {
    @Mapping(source = "createdTimestamp", target = "registerDate")
    @Mapping(source = "id", target = "userUuid")
    @Mapping(source = "realmRoles", target ="roles")
    UserDTO toDto(UserRepresentation userRepresentation);

    default LocalDateTime toLocalDateTime(Long createdTimestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), TimeZone.getDefault().toZoneId());
    }
}
