package com.unina.natourkt.domain.model

import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.presentation.base.ui_state.UserUiState

/**
 * User domain model (to improve)
 */
data class User(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val profilePhoto: String?
)

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        profilePhoto = profilePhoto
    )
}

fun User.toUi(): UserUiState {
    return UserUiState(
        id = id,
        username = username,
        isAdmin = isAdmin,
        photo = profilePhoto,
    )
}

suspend fun User.convertKeys(execute: suspend (string: String) -> String): User {
    val newPhoto = this.profilePhoto?.let { execute(it) }
    return this.copy(profilePhoto = newPhoto)
}