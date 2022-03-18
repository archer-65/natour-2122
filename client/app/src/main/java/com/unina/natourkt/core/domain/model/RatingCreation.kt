package com.unina.natourkt.core.domain.model

import com.unina.natourkt.core.util.Difficulty

data class RatingCreation(
    val difficulty: Difficulty,
    val duration: Float,
    val author: User?,
    val ratedRouteId: Long
)