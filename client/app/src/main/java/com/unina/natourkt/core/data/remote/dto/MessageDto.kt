package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("message_id")
    @SerializedName("message_id")
    val messageId: Long,

    @SerialName("message_content")
    @SerializedName("message_content")
    val messageContent: String,

    @SerialName("sent_on")
    @SerializedName("sent_on")
    val sentOn: String,

    @SerialName("sender_id")
    @SerializedName("sender_id")
    val senderId: Long,

    @SerialName("recipient_id")
    @SerializedName("recipient_id")
    val recipientId: Long,

    @SerialName("chat_id")
    @SerializedName("chat_id")
    val chatId: Long,
)