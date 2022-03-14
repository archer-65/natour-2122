package com.unina.natourkt.feature_post.post_details

import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.presentation.model.PostDetailsUi
import com.unina.natourkt.core.presentation.model.UserUi

data class PostDetailsUiState(
    val isLoading: Boolean = false,
    val error: DataState.Cause? = null,
    val post: PostDetailsUi? = null,
    val loggedUser: UserUi? = null
)