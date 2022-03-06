package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.domain.model.User

/**
 * Response coming from API for [User]
 */
data class UserDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("username")
    val username: String,

    @SerializedName("photo")
    val photo: String?,
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