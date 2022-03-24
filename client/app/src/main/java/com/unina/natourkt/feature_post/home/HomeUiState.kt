package com.unina.natourkt.feature_post.home

import com.unina.natourkt.core.presentation.model.UserUi

data class HomeUiState(
    val isLoading: Boolean = false,
    val loggedUser: UserUi? = null
)

