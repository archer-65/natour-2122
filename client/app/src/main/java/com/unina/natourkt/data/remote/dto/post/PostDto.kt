package com.unina.natourkt.data.remote.dto.post

import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.model.Route

data class PostDto(
    val description: String,
    val id: Long,
    val photos: List<PostPhotoDto>,
    val reported: Boolean,
    val user: UserDto,
    val routeId: Long,
    val routeTitle: String,
)

fun PostDto.toPost(): Post {
    return Post(
        id = id,
        description = description,
        isReported = reported,
        photos = photos.map { photo -> photo.photoUrl },
        user = user.toUser(),
        route = Route(
            id = routeId,
            title = routeTitle,
        )
    )
}