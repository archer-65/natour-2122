package com.unina.natourkt.domain.model.route

/**
 * RouteStop represents each Stop for a [Post]
 */
data class RouteStop(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)
