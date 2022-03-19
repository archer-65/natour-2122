package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatDto(
    @SerializedName("chat_id")
    val chatId: Long,

    @SerializedName("chat_creation_date")
    val chatCreationDate: String,

    @SerializedName("chat_user_1")
    val chatUser1: UserDto,

    @SerializedName("chat_user_2")
    val chatUser2: UserDto,
)