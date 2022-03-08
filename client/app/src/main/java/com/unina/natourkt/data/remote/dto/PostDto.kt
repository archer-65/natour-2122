package com.unina.natourkt.data.remote.dto.post

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.data.remote.dto.RouteTitleDto
import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.data.remote.dto.toRouteTitle
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.model.route.Route

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
    val photos: List<PostPhotoDto>,

    @SerializedName("post_author")
    val author: UserDto,

    @SerializedName("tagged_route")
    val taggedRoute: RouteTitleDto
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
        taggedRoute = taggedRoute.toRouteTitle()
    )
}