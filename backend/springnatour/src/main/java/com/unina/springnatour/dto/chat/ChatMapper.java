package com.unina.springnatour.dto.chat;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.chat.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper extends BaseMapper<Chat, ChatDto> {

    @Override
    @Mappings({
            @Mapping(target = "chatId", source = "id"),
            @Mapping(target = "chatCreationDate", source = "creationDate"),
            @Mapping(target = "chatUser1", source = "user1"),
            @Mapping(target = "chatUser2", source = "user2")
    })
    ChatDto toDto(Chat chat);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "chatId"),
            @Mapping(target = "creationDate", source = "chatCreationDate"),
            @Mapping(target = "user1", source = "chatUser1"),
            @Mapping(target = "user2", source = "chatUser2")
    })
    Chat toEntity(ChatDto chatDto);

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
