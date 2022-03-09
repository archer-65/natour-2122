package com.unina.natourkt.presentation.base.ui_state

/**
 * Represents the data class of a route element
 */
data class RouteItemUiState(
    val id: Long,
    val title: String,
    val avgDifficulty: Int,
    val disabilityFriendly: Boolean,
    val previewPhoto: String,
    val authorId: Long,
)

suspend fun RouteItemUiState.convertKeys(execute: suspend (string: String) -> String): RouteItemUiState {
    val newPreview = execute(this.previewPhoto)
    return this.copy(previewPhoto = newPreview)
}
