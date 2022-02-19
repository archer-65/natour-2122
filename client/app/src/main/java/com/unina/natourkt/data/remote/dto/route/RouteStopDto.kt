package com.unina.natourkt.data.remote.dto.route

import com.unina.natourkt.domain.model.RouteStop

data class RouteStopDto(
    val id: Long,
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

fun RouteStopDto.toRouteStop(): RouteStop {
    return RouteStop(
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude
    )
}