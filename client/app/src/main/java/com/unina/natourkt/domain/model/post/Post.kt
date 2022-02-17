package com.unina.natourkt.domain.model.post

import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.home.PostItemUiState

data class Post(
    val id: Long,
    val description: String,
    val isReported: Boolean = false,
    val photos: List<PostPhoto>,
    val user: User,
)

fun Post.toUi(): PostItemUiState {
    return PostItemUiState(
        id = id,
        description = description,
        photos = photos.map { photo -> photo.photo },
        authorId = user.id,
        authorUsername = user.username,
        authorPhoto = user.photo,
        //routeId =
        //routeTitle =
    )
}
