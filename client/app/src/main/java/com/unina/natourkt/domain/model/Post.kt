package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.base.model.PostGridItemUiState
import com.unina.natourkt.presentation.base.model.PostItemUiState
import com.unina.natourkt.presentation.post_details.PostUiState

/**
 * Post model (to improve)
 */
data class Post(
    val id: Long,
    val description: String,
    val isReported: Boolean,
    val photos: List<String>,
    val author: User,
    val taggedRoute: RouteTitle,
)

/**
 * Function to map Post to [PostItemUiState]
 */
fun Post.toUi(): PostItemUiState {
    return PostItemUiState(
        id = id,
        description = description,
        photos = photos,
        authorId = author.id,
        authorUsername = author.username,
        authorPhoto = author.profilePhoto,
        routeId = taggedRoute.id,
        routeTitle = taggedRoute.title
    )
}

/**
 * Function to map Post to [PostGridItemUiState]
 */
fun Post.toGridUi(): PostGridItemUiState {
    return PostGridItemUiState(
        id = id,
        previewPhoto = photos.first(),
        authorId = author.id
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
        authorId = author.id,
        authorUsername = author.username,
        authorPhoto = author.profilePhoto,
        routeId = taggedRoute.id,
        routeTitle = taggedRoute.title
    )
}