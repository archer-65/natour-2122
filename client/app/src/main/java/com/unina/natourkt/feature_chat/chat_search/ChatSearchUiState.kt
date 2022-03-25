package com.unina.natourkt.feature_chat.chat_search

import com.unina.natourkt.core.presentation.model.ChatItemUi

data class ChatSearchUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val retrievedChat: ChatItemUi? = null,
)
