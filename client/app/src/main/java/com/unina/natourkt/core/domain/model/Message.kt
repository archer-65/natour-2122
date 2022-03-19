package com.unina.natourkt.core.domain.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Message(
    val id: Long,
    val content: String,
    val sentOn: LocalDateTime,
    val senderId: Long,
    val recipientId: Long,
    val chatId: Long,
)