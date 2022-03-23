package com.unina.natourkt.feature_chat.chat_search

sealed class ChatSearchEvent {
    data class EnteredQuery(val query: String) : ChatSearchEvent()
}