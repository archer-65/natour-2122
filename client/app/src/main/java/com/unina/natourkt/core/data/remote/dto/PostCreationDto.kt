package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostCreationDto(
    @SerializedName("post_description")
    val description: String,

    @SerializedName("post_photos")
    val photos: List<Photo>,

    @SerializedName("post_author")
    val author: UserDto,

    @SerializedName("tagged_route")
    val taggedRoute: RouteTitleDto,
) {

    data class Photo(
        @SerializedName("photo")
        val photo: String,
    )
}


