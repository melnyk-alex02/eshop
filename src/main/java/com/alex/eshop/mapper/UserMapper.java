package com.alex.eshop.mapper;

import com.alex.eshop.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(source = "createdTimestamp", target = "registerDate")
    UserDTO toDto(UserRepresentation userRepresentation);
}
