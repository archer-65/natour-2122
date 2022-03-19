package com.unina.natourkt.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageCreationDto(
    @SerialName("message_content")
    val messageContent: String,
    @SerialName("sender_id")
    val senderId: Long,
    @SerialName("recipient_id")
    val recipientId: Long,
    @SerialName("chat_id")
    val chatId: Long,
)