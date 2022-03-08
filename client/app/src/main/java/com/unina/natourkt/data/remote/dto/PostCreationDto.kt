package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostCreationDto(
    @SerializedName("post_description")
    val description: String,

    @SerializedName("post_photos")
    val photos: List<PostPhotoCreationDto>,

    @SerializedName("post_author")
    val author: UserDto,

    @SerializedName("tagged_route")
    val taggedRoute: RouteTitleDto,
)

data class PostPhotoCreationDto(
    @SerializedName("photo")
    val photo: String,
)
