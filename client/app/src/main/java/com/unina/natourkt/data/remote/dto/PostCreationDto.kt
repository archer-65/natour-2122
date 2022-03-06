package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostCreationDto(
    @SerializedName("description")
    val description: String,

    @SerializedName("photos")
    val photos: List<PostPhotoCreationDto>,

    @SerializedName("user")
    val author: UserDto,

    @SerializedName("routeId")
    val routeId: Long
)

data class PostPhotoCreationDto(
    @SerializedName("photo")
    val photo: String,
)
