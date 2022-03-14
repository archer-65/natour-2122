package com.unina.natourkt.presentation.route_details

import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.route.RouteStop
import com.unina.natourkt.presentation.base.model.UserUiState
import java.time.LocalDateTime

data class RouteDetailsUiState(
    val isLoading: Boolean = false,
    val error: DataState.Cause? = null,
    val route: RouteUiState? = null,
    val loggedUser: UserUiState? = null,
)

data class RouteUiState(
    val id: Long,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val duration: Double,
    val disabilityFriendly: Boolean,
    val modifiedDate: LocalDateTime?,
    val isReported: Boolean,
    val photos: List<String>,
    val stops: List<RouteStopUiState>,
    val authorId: Long,
    val polylineOptions: PolylineOptions = PolylineOptions(),
)

data class RouteStopUiState(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

enum class Difficulty(val difficultyValue: Int) {
    EASY(1),
    MEDIUM(2),
    HARD(3),
}

fun RouteStopUiState.toRouteStop(): RouteStop {
    return RouteStop(
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude,
    )
}

suspend fun RouteUiState.convertKeys(execute: suspend (string: String) -> String): RouteUiState {
    val photos = this.photos.map {
        execute(it)
    }

    return this.copy(photos = photos)
}