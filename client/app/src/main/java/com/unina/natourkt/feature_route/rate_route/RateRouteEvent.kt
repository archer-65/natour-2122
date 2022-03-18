package com.unina.natourkt.feature_route.rate_route

import com.unina.natourkt.core.util.Difficulty

sealed class RateRouteEvent {
    object Upload : RateRouteEvent()

    data class EnteredDuration(val duration: String) : RateRouteEvent()
    data class EnteredDifficulty(val difficulty: Difficulty) : RateRouteEvent()
}