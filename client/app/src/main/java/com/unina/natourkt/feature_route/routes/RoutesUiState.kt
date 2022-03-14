package com.unina.natourkt.feature_route.routes

import androidx.paging.PagingData
import com.unina.natourkt.core.presentation.model.RouteItemUi

data class RoutesUiState(
    val routeItems: PagingData<RouteItemUi> = PagingData.empty(),
)