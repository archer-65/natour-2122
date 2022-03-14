package com.unina.natourkt.feature_route.route_details

import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.presentation.model.RouteDetailsUi
import com.unina.natourkt.core.presentation.model.UserUi

data class RouteDetailsUiState(
    val isLoading: Boolean = false,
    val error: DataState.Cause? = null,
    val route: RouteDetailsUi? = null,
    val loggedUser: UserUi? = null,
)