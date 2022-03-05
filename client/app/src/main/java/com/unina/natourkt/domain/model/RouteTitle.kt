package com.unina.natourkt.domain.model
import com.unina.natourkt.presentation.new_post.UpcomingRoute

data class RouteTitle(
    val title: String,
    val id: Long
)

fun RouteTitle.toUi(): UpcomingRoute{
    return UpcomingRoute(
        routeTitle = title,
        routeId = id
    )
}