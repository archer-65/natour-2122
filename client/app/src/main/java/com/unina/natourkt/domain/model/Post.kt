package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.home.PostItemUiState

data class Post(
    val id: Long,
    val description: String,
    val isReported: Boolean = false,
    val photos: List<String>,
    val user: User,
    val route: Route,
)

fun Post.toUi(): PostItemUiState {
    return PostItemUiState(
        id = id,
        description = description,
        photos = photos,
        authorId = user.id,
        authorUsername = user.username,
        authorPhoto = user.photo,
        routeId = route.id,
        routeTitle = route.title
    )
}
