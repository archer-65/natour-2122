package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.post.Post

data class PostDto(
    val description: String,
    val id: Long,
    val photos: List<PostPhotoDto>,
    val reported: Boolean,
    val routeId: Long,
    val routeTitle: String,
    val user: UserDto
)

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        description = description,
        isReported = reported,
        photos = photos.map { photo -> photo.toPostPhoto() },
        user = user.toUser()
    )
}