package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.RouteTitle

data class RouteTitleDto(
    val title: String,
    val id: Long
)

fun RouteTitleDto.toRouteTitle(): RouteTitle {

    return RouteTitle(
        title = title,
        id = id
    )
}

