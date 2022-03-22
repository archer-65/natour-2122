package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUi(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val photo: String? = null
) : Parcelable {

    suspend fun convertKeys(execute: suspend (string: String) -> String): UserUi {
        val newPhoto = this.photo?.let { execute(it) }
        return this.copy(photo = newPhoto)
    }
}

