package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.ChatDto
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.util.DateTimeParser
import javax.inject.Inject

class ChatApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper
) : ApiMapper<ChatDto, Chat> {

    override fun mapToDomain(apiEntity: ChatDto): Chat {
        return Chat(
            id = apiEntity.chatId,
            creationDate = DateTimeParser.parseDate(apiEntity.chatCreationDate),
            firstMember = userApiMapper.mapToDomain(apiEntity.chatUser1),
            secondMember = userApiMapper.mapToDomain(apiEntity.chatUser2),
        )
    }
}