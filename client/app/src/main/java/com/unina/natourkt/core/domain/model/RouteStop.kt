package com.unina.natourkt.core.domain.model

/**
 * RouteStop represents each Stop for a [Route]
 */
data class RouteStop(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)
