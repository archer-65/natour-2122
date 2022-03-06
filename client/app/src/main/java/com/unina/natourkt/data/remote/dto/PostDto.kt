package com.unina.natourkt.data.remote.dto.post

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.model.route.Route

data class PostDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("description")
    val description: String?,

    @SerializedName("reported")
    val isReported: Boolean,

    @SerializedName("creationDate")
    val creationDate: String,

    @SerializedName("photos")
    val photos: List<PostPhotoDto>,

    @SerializedName("user")
    val author: UserDto,

    @SerializedName("routeId")
    val routeId: Long,

    @SerializedName("routeTile")
    val routeTitle: String,
)


data class PostPhotoDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("photo")
    val photo: String
)

/**
 * Function to map [PostDto] to [Post]
 */
fun PostDto.toPost(): Post {
    return Post(
        id = id,
        description = description ?: "",
        isReported = isReported,
        photos = photos.map { photo -> photo.photo },
        author = author.toUser(),
        routeId = routeId,
        routeTitle = routeTitle
    )
}