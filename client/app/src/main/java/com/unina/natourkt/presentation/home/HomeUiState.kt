package com.unina.natourkt.presentation.home

import com.unina.natourkt.common.DataState

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val postItems: MutableList<PostItemUiState> = mutableListOf(),
    val currentPage: Int = 0,
)

data class PostItemUiState(
    val id: Long,
    val description: String,
    val photos: List<String> = listOf(),
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String?,
    // Da modificare perchè manca Route
    val routeId: Long? = null,
    val routeTitle: String? = null,
)