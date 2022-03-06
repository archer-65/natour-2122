package com.unina.natourkt.domain.model

import com.unina.natourkt.data.remote.dto.PostCreationDto
import com.unina.natourkt.data.remote.dto.PostPhotoCreationDto

data class PostCreation(
    val description: String,
    val photos: List<String>,
    val author: User? = null,
    val routeId: Long,
    val routeTitle: String,
)

fun PostCreation.toCreationDto(): PostCreationDto {
    return PostCreationDto(
        description = description,
        photos = photos.map {
            PostPhotoCreationDto(
                it
            )
        },
        author = author!!.toDto(),
        routeId = routeId,
    )
}