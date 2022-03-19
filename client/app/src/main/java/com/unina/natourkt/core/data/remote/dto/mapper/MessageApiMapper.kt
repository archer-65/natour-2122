package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.MessageDto
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.util.DateTimeParser
import javax.inject.Inject

class MessageApiMapper @Inject constructor() : ApiMapper<MessageDto, Message> {

    override fun mapToDomain(apiEntity: MessageDto): Message {
        return Message(
            id = apiEntity.messageId,
            content = apiEntity.messageContent,
            sentOn = DateTimeParser.parseDateTime(apiEntity.sentOn),
            senderId = apiEntity.senderId,
            recipientId = apiEntity.recipientId,
            chatId = apiEntity.chatId
        )
    }
}