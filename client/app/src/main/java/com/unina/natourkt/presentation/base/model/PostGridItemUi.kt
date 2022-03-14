package com.unina.natourkt.presentation.base.model

/**
 * Represents the data class of a post (home screen) element
 */
data class PostGridItemUiState(
    val id: Long,
    val previewPhoto: String,
    val authorId: Long,
)

suspend fun PostGridItemUiState.convertKeys(execute: suspend (string: String) -> String): PostGridItemUiState {
    val previewPhoto = execute(this.previewPhoto)
    return this.copy(previewPhoto = previewPhoto)
}