package com.unina.natourkt.presentation.home

import androidx.paging.PagingData
import com.unina.natourkt.common.DataState

data class HomeUiState(
    val postItems: PagingData<PostItemUiState>? = null,
)

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