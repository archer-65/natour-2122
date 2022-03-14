package com.unina.natourkt.feature_route.new_route

import android.net.Uri
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.model.RouteStop

data class NewRouteUiState(
    val isLoadedFromGPX: Boolean = false,
    val isInserted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val routeInfo: NewRouteInfo = NewRouteInfo(),
    val routeStops: List<NewRouteStop> = emptyList(),
    val routePhotos: List<Uri> = emptyList(),
    val polylineOptions: PolylineOptions = PolylineOptions()
)

data class NewRouteInfo(
    val routeTitle: String = "",
    val routeDescription: String = "",
    val duration: String = "1",
    val disabilityFriendly: Boolean = false,
    val difficulty: Difficulty = Difficulty.EASY,
)

data class NewRouteStop(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

enum class Difficulty(val difficultyValue: Int) {
    EASY(1),
    MEDIUM(2),
    HARD(3),
}

fun NewRouteStop.toRouteStopCreation(): RouteStop {
    return RouteStop(
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude
    )
}

fun NewRouteUiState.toRouteCreation(): RouteCreation {
    return RouteCreation(
        title = routeInfo.routeTitle,
        description = routeInfo.routeDescription,
        avgDifficulty = routeInfo.difficulty.difficultyValue,
        avgDuration = routeInfo.duration.toDouble(),
        disabilityFriendly = routeInfo.disabilityFriendly,
        photos = routePhotos.map { it.toString() },
        stops = routeStops.map { it.toRouteStopCreation() },
        author = null
    )
}