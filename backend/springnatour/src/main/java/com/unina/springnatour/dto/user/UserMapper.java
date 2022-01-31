package com.unina.springnatour.dto.user;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Override
    UserDto toDto(User user);

    @Override
    User toEntity(UserDto userDto);
}
