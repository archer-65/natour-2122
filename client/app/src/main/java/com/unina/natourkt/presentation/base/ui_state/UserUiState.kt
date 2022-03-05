package com.unina.natourkt.presentation.base.ui_state

import com.unina.natourkt.domain.model.User

data class UserUiState(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val photo: String? = null
)

suspend fun UserUiState.convertKeys(execute: suspend (string: String) -> String): UserUiState {
    val newPhoto = this.photo?.let { execute(it) }
    return this.copy(photo = newPhoto)
}