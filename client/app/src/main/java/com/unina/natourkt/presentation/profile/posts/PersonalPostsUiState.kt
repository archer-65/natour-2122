package com.unina.natourkt.presentation.profile.posts

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.ui_state.PostGridItemUiState

data class PersonalPostsUiState(
    val postItems: PagingData<PostGridItemUiState>? = null,
)
