package com.unina.natourkt.presentation.routes

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState

data class RouteUiState(
    val routeItems: PagingData<RouteItemUiState> = PagingData.empty(),
)