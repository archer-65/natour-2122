package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.User

data class UserDto(
    val id: Long,
    val photoUrl: String,
    val username: String
)

fun UserDto.toUser(): User {
    return User(
        id = id,
        username = username,
        photo = photoUrl,
    )
}