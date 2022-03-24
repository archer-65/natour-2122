package com.unina.natourkt.feature_chat.chat

sealed class ChatEvent {
    data class UpdateMessage(val body: String) : ChatEvent()
    object ReadAll : ChatEvent()
    object SendMessage : ChatEvent()
}