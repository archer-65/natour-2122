package com.unina.natourkt.presentation.post_details

import com.unina.natourkt.common.DataState

data class PostDetailsUiState(
    val isLoading: Boolean = false,
    val error: DataState.CustomMessage? = null,
    val post: PostUiState? = null
)

data class PostUiState(
    val id: Long,
    val description: String,
    val photos: List<String>,
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String?,
    val routeId: Long?,
    val routeTitle: String
)

suspend fun PostUiState.convertKeys(execute: suspend (string: String) -> String): PostUiState {
    val authorPhoto = this.authorPhoto?.let { execute(it) }
    val photos = this.photos.map {
        execute(it)
    }

    return this.copy(photos = photos, authorPhoto = authorPhoto)
}