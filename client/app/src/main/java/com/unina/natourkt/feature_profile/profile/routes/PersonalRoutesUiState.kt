package com.unina.natourkt.feature_profile.profile.routes

import androidx.paging.PagingData
import com.unina.natourkt.core.presentation.model.RouteItemUi

data class PersonalRoutesUiState(
    val routeItems: PagingData<RouteItemUi> = PagingData.empty()
)