package com.unina.natourkt.feature_chat.chat_list

sealed class ChatListEvent {
    object ClickChat: ChatListEvent()
}