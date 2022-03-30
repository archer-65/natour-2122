package com.unina.natourkt.core.presentation.model

import java.time.LocalDateTime

data class MessageItemUi(
    val messageId: Long,
    val sentOn: LocalDateTime,
    val content: String,
    override val type: MessageType,
) : ChatGenericUi