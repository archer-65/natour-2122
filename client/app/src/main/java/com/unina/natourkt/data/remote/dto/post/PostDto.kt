package com.unina.natourkt.data.remote.dto.post

import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.model.route.Route
import java.time.LocalDateTime

/**
 * This class represents the response from API for [Post]
 */
data class PostDto(
    val id: Long,
    val description: String,
    val reported: Boolean,
    val creationDate: String,
    val photos: List<PostPhotoDto>,
    val user: UserDto,
    val routeId: Long,
    val routeTitle: String,
)

/**
 * Function to map [PostDto] to [Post]
 */
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