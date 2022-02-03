package com.unina.springnatour.dto.chat;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.chat.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper extends BaseMapper<Message, MessageDto> {

    @Override
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "recipientId", source = "recipient.id")
    @Mapping(target = "chatId", source = "chat.id")
    MessageDto toDto(Message message);

    @Override
    @Mapping(target = "sender.id", source = "senderId")
    @Mapping(target = "recipient.id", source = "recipientId")
    @Mapping(target = "chat.id", source = "chatId")
    Message toEntity(MessageDto messageDto);
}
