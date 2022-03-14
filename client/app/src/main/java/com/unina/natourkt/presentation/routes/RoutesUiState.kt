package com.unina.natourkt.presentation.routes

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.model.RouteItemUiState

data class RoutesUiState(
    val routeItems: PagingData<RouteItemUiState> = PagingData.empty(),
)