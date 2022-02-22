package com.unina.natourkt.presentation.base.ui_state

/**
 * Represents the data class of a route element
 */
data class RouteItemUiState(
    val id: Long,
    val title: String,
    val avgDifficulty: Int,
    val disabledFriendly: Boolean,
    val previewPhoto: String,
)
