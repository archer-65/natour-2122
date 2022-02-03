package com.unina.springnatour.dto.chat;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.chat.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper extends BaseMapper<Chat, ChatDto> {

    @Override
    ChatDto toDto(Chat chat);

    @Override
    Chat toEntity(ChatDto chatDto);
}
