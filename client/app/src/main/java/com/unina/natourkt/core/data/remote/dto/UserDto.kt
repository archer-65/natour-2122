package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.core.domain.model.User

/**
 * Response coming from API for [User]
 */
data class UserDto(
    @SerializedName("user_id")
    val id: Long,

    @SerializedName("username")
    val username: String,

    @SerializedName("profile_photo")
    val profilePhoto: String?,
)