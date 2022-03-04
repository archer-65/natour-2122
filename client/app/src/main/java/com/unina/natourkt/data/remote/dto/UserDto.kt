package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.User

/**
 * Response coming from API for [User]
 */
data class UserDto(
    val id: Long,
    val photo: String?,
    val username: String
)

/**
 * Map [UserDto] to [User]
 */
fun UserDto.toUser(): User {
    return User(
        id = id,
        username = username,
        photo = photo,
    )
}