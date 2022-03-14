package com.unina.natourkt.presentation.profile.posts

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.model.PostGridItemUiState

data class PersonalPostsUiState(
    val postItems: PagingData<PostGridItemUiState> = PagingData.empty()
)
