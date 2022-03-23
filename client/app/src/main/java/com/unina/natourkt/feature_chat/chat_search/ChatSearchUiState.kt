package com.unina.natourkt.feature_chat.chat_search

import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.UserUi

data class ChatSearchUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val retrievedChat: ChatItemUi? = null,
)
