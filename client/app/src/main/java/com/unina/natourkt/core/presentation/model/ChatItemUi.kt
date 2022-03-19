package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatItemUi(
    val chatId: Long,
    val otherMemberId: Long,
    val otherMemberUsername: String,
    val otherMemberPhoto: String
) : Parcelable {

    suspend fun convertKeys(execute: suspend (string: String) -> String): ChatItemUi {
        val newPhoto = execute(this.otherMemberPhoto)
        return this.copy(otherMemberPhoto = newPhoto)
    }
}