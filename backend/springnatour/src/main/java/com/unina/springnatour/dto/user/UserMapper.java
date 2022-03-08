package com.unina.springnatour.dto.user;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Override
    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "profilePhoto", source = "photo"),
    })
    UserDto toDto(User user);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "photo", source = "profilePhoto"),
    })
    User toEntity(UserDto userDto);
}
