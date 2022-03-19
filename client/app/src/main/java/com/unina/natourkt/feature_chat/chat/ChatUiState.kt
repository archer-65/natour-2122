package com.unina.natourkt.feature_chat.chat

import com.unina.natourkt.core.presentation.model.ChatGenericUi
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.MessageItemUi

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatGenericUi> = emptyList(),
    val chatInfo: ChatItemUi
)