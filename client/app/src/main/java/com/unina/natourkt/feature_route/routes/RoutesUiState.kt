package com.unina.natourkt.feature_route.routes

import com.unina.natourkt.core.presentation.model.UserUi

data class RoutesUiState(
    val isLoading: Boolean = false,
    val loggedUser: UserUi? = null,
)