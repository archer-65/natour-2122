package com.unina.natourkt.domain.model

import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.presentation.base.ui_state.PostGridItemUiState
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState
import com.unina.natourkt.presentation.post_details.PostUiState

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

/**
 * Function to map Post to [PostGridItemUiState]
 */
fun Post.toGridUi(): PostGridItemUiState {
    return PostGridItemUiState(
        id = id,
        previewPhoto = photos.first(),
        authorId = user.id
    )
}

/**
 * Function to map Post to [PostItemUiState]
 */
fun Post.toDetailUi(): PostUiState {
    return PostUiState(
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