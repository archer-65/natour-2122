package com.unina.natourkt.core.presentation.model

/**
 * Represents the data class of a post (home screen) element
 */
data class PostGridItemUi(
    val id: Long,
    val previewPhoto: String,
    val authorId: Long,
) {

    suspend fun convertKeys(execute: suspend (string: String) -> String): PostGridItemUi {
        val previewPhoto = execute(this.previewPhoto)
        return this.copy(previewPhoto = previewPhoto)
    }
}

