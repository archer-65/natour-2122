package com.unina.natourkt.domain.model

import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.presentation.home.PostItemUiState

/**
 * Post model (to improve)
 */
data class Post(
    val id: Long,
    val description: String,
    val isReported: Boolean = false,
    val photos: List<String>,
    val user: User,
    val route: Route,
)

/**
 * Function to map Post to [PostItemUiState]
 */
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
