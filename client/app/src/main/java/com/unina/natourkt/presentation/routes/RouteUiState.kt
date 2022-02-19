package com.unina.natourkt.presentation.routes

import androidx.paging.PagingData

data class RouteUiState(
    val routeItems: PagingData<RouteItemUiState>? = null,
)

data class RouteItemUiState(
    val id: Long,
    val title: String,
    val avgDifficulty: Int,
    val disabledFriendly: Boolean,
    val previewPhoto: String,
)
