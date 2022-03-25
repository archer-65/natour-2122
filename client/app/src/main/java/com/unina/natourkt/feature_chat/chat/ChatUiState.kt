package com.unina.natourkt.feature_chat.chat

import com.unina.natourkt.core.presentation.model.ChatGenericUi
import com.unina.natourkt.core.presentation.model.ChatItemUi

data class ChatUiState(
    val isLoading: Boolean = false,
    val chatInfo: ChatItemUi,
    val messages: List<ChatGenericUi> = emptyList(),
    val newMessagesNumber: Int = 0,
    val messageState: String = "",
    val shouldScrollToBottom: Boolean = false,
) {

    val shouldResetBadge: Boolean
        get() = newMessagesNumber == 0

    val shouldResetText: Boolean
        get() = messageState.isBlank()

}