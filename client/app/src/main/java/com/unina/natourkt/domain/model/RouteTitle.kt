package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.new_post.RouteTitleItemUiState

data class RouteTitle(
    val id: Long,
    val title: String,
)

fun RouteTitle.toUi(): RouteTitleItemUiState {
    return RouteTitleItemUiState(
        routeId = id,
        routeTitle = title,
    )
}