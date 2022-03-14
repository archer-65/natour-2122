package com.unina.natourkt.core.domain.model

data class Filter(
    val query: String,
    val latitude: Double?,
    val longitude: Double?,
    val distance: Float,
    val minDuration: Int?,
    val maxDuration: Int?,
    val minDifficulty: Int?,
    val isDisabilityFriendly: Boolean?
)