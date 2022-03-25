package com.unina.natourkt.feature_route.route_search

import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.core.domain.model.Filter
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.util.Difficulty

data class RouteSearchUiState(
    val loggedUser: UserUi? = null,

    val query: String = "",
    val place: Place? = null,
    val distance: Float = 5F,
    val minDuration: Int? = null,
    val maxDuration: Int? = null,
    val minDifficulty: Difficulty? = null,
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
        minDifficulty = minDifficulty?.value,
        isDisabilityFriendly = isDisabilityFriendly
    )
}
