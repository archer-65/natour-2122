package com.unina.natourkt.domain.model

import com.unina.natourkt.data.remote.dto.PostCreationDto

data class PostCreation(
    val description: String,
    val photos: List<String>,
    val author: User? = null,
    val taggedRoute: RouteTitle,
)

fun PostCreation.toCreationDto(): PostCreationDto {
    return PostCreationDto(
        description = description,
        photos = photos.map {
            PostCreationDto.Photo(
                it
            )
        },
        author = author!!.toDto(),
        taggedRoute = taggedRoute.toRouteTitleDto(),
    )
}