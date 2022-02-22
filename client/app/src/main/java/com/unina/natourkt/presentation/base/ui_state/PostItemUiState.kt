package com.unina.natourkt.presentation.base.ui_state

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