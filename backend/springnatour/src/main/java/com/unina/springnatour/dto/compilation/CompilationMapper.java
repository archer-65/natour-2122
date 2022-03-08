package com.unina.springnatour.dto.compilation;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.model.Compilation;
import com.unina.springnatour.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompilationMapper extends BaseMapper<Compilation, CompilationDto> {

    @Override
    @Mappings({
            @Mapping(target = "compilationId", source = "id"),
            @Mapping(target = "compilationTitle", source = "title"),
            @Mapping(target = "compilationDescription", source = "description"),
            @Mapping(target = "compilationCreationDate", source = "creationDate"),
            @Mapping(target = "compilationPhoto", source = "photo"),
            @Mapping(target = "author", source = "user")
    })
    CompilationDto toDto(Compilation compilation);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "compilationId"),
            @Mapping(target = "title", source = "compilationTitle"),
            @Mapping(target = "description", source = "compilationDescription"),
            @Mapping(target = "creationDate", source = "compilationCreationDate"),
            @Mapping(target = "photo", source = "compilationPhoto"),
            @Mapping(target = "user", source = "author")
    })
    Compilation toEntity(CompilationDto compilationDto);

    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "profilePhoto", source = "photo"),
    })
    UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "photo", source = "profilePhoto"),
    })
    User toEntity(UserDto userDto);
}
