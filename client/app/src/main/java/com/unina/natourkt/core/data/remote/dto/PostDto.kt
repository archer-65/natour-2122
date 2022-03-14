package com.unina.natourkt.core.data.remote.dto.post

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.core.data.remote.dto.RouteTitleDto
import com.unina.natourkt.core.data.remote.dto.UserDto

data class PostDto(
    @SerializedName("post_id")
    val id: Long,

    @SerializedName("post_description")
    val description: String?,

    @SerializedName("is_post_reported")
    val isReported: Boolean,

    @SerializedName("post_creation_date")
    val creationDate: String,

    @SerializedName("post_photos")
    val photos: List<PhotoDto>,

    @SerializedName("post_author")
    val author: UserDto,

    @SerializedName("tagged_route")
    val taggedRoute: RouteTitleDto
) {

    data class PhotoDto(
        @SerializedName("id")
        val id: Long?,

        @SerializedName("photo")
        val photo: String
    )

}