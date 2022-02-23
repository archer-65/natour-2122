package com.unina.natourkt.presentation.profile.routes

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState

data class PersonalRoutesUiState(
    val routeItems: PagingData<RouteItemUiState> = PagingData.empty()
)