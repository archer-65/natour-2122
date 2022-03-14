package com.unina.natourkt.presentation.base.model

/**
 * Represents the data class of a post (home screen) element
 */
data class PostItemUiState(
    val id: Long?,
    val description: String?,
    val photos: List<String> = listOf(),
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String?,
    val routeId: Long?,
    val routeTitle: String,
)

suspend fun PostItemUiState.convertKeys(execute: suspend (string: String) -> String): PostItemUiState {
    val authorPhoto = this.authorPhoto?.let { execute(it) }
    val photos = this.photos.map {
        execute(it)
    }

    return this.copy(photos = photos, authorPhoto = authorPhoto)
}
