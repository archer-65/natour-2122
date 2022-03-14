package com.unina.natourkt.core.presentation.model

import com.unina.natourkt.core.util.Difficulty

/**
 * Represents the data class of a route element
 */
data class RouteItemUi(
    val id: Long,
    val title: String,
    val avgDifficulty: Difficulty,
    val disabilityFriendly: Boolean,
    val previewPhoto: String,
    val authorId: Long,
) {

    suspend fun convertKeys(execute: suspend (string: String) -> String): RouteItemUi {
        val newPreview = execute(this.previewPhoto)
        return this.copy(previewPhoto = newPreview)
    }
}


