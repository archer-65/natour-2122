package com.unina.natourkt.core.domain.model

data class DirectionsRequest(
    val origin: String,
    val destination: String,
    val waypoints: String
)
