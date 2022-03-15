package com.unina.natourkt.core.domain.model

import com.unina.natourkt.core.util.Difficulty

data class RouteCreation(
    val title: String,
    val description: String,
    val avgDifficulty: Difficulty,
    val avgDuration: Double,
    val disabilityFriendly: Boolean,
    val photos: List<String>,
    val stops: List<RouteStop>,
    val author: User?,
)