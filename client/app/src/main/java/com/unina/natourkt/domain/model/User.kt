package com.unina.natourkt.domain.model

import com.unina.natourkt.data.remote.dto.UserDto

/**
 * User domain model (to improve)
 */
data class User(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val photo: String? = null
)

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        photo = photo
    )
}

suspend fun User.convertKeys(execute: suspend (string: String) -> String): User {
    val newPhoto = this.photo?.let { execute(it) }
    return this.copy(photo = newPhoto)
}