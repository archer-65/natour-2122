package com.unina.natourkt.presentation.home

import androidx.paging.PagingData

/**
 * Contains only paginated data of [PostItemUiState]
 */
data class HomeUiState(
    val postItems: PagingData<PostItemUiState>? = null,
)

/**
 * Represents the data class of a post (home screen) element
 */
data class PostItemUiState(
    val id: Long,
    val description: String,
    val photos: List<String> = listOf(),
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String?,
    val routeId: Long,
    val routeTitle: String,
)