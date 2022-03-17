package com.unina.natourkt.feature_profile.profile.posts

import androidx.paging.PagingData
import com.unina.natourkt.core.presentation.model.PostGridItemUi

data class ProfilePostsUiState(
    val postItems: PagingData<PostGridItemUi> = PagingData.empty()
)