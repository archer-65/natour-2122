package com.unina.natourkt.presentation.home

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState

/**
 * Contains only paginated data of [PostItemUiState]
 */
data class HomeUiState(
    val postItems: PagingData<PostItemUiState> = PagingData.empty()
)

