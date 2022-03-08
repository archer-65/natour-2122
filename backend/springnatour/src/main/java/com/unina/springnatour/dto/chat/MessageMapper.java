package com.unina.springnatour.dto.chat;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.chat.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper extends BaseMapper<Message, MessageDto> {

    @Override
    @Mappings({
            @Mapping(target = "messageId", source = "id"),
            @Mapping(target = "messageContent", source = "content"),
            @Mapping(target = "sentOn", source = "sentOn"),
            @Mapping(target = "senderId", source = "sender.id"),
            @Mapping(target = "recipientId", source = "recipient.id"),
            @Mapping(target = "chatId", source = "chat.id"),
    })
    MessageDto toDto(Message message);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "messageId"),
            @Mapping(target = "content", source = "messageContent"),
            @Mapping(target = "sentOn", source = "sentOn"),
            @Mapping(target = "sender.id", source = "senderId"),
            @Mapping(target = "recipient.id", source = "recipientId"),
            @Mapping(target = "chat.id", source = "chatId"),
    })
    Message toEntity(MessageDto messageDto);
}
