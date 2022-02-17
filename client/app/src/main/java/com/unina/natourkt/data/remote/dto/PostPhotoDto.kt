package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.post.PostPhoto

data class PostPhotoDto(
    val id: Long,
    val photoUrl: String
)

fun PostPhotoDto.toPostPhoto(): PostPhoto {
    return PostPhoto(
        id = id,
        photo = photoUrl
    )
}