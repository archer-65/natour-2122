package com.unina.natourkt.core.domain.model

import android.util.Log

data class RouteCreation(
    val title: String,
    val description: String,
    val avgDifficulty: Int,
    val avgDuration: Double,
    val disabilityFriendly: Boolean,
    val photos: List<String>,
    val stops: List<RouteStop>,
    val author: User?,
)