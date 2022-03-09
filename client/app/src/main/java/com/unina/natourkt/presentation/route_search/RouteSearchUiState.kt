package com.unina.natourkt.presentation.route_search

import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.domain.model.Filter
import kotlin.math.max

data class RouteSearchUiState(
    val query: String = "",
    val place: Place? = null,
    val distance: Float = 5F,
    val minDuration: Int? = null,
    val maxDuration: Int? = null,
    val minDifficulty: Difficulty = Difficulty.NONE,
    val isDisabilityFriendly: Boolean? = null,
)

fun RouteSearchUiState.toFilter(): Filter {
    return Filter(
        query = query,
        latitude = place?.latLng?.latitude,
        longitude = place?.latLng?.longitude,
        distance = distance,
        minDuration = minDuration,
        maxDuration = maxDuration,
        minDifficulty = minDifficulty.difficultyValue,
        isDisabilityFriendly = isDisabilityFriendly
    )
}

enum class Difficulty(val difficultyValue: Int) {
    EASY(1),
    MEDIUM(2),
    HARD(3),
    NONE(0)
}
