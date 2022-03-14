package com.unina.natourkt.domain.model.route

import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.base.model.RouteItemUiState
import com.unina.natourkt.presentation.route_details.RouteStopUiState
import com.unina.natourkt.presentation.route_details.RouteUiState
import java.time.LocalDateTime

/**
 * Route model (to improve)
 */
data class Route(
    val id: Long,
    val title: String,
    val description: String,
    val difficulty: Int,
    val duration: Double,
    val disabilityFriendly: Boolean,
    val creationDate: LocalDateTime,
    val modifiedDate: LocalDateTime?,
    val isReported: Boolean,
    val photos: List<String>,
    val stops: List<RouteStop>,
    val author: User
)

/**
 * RouteStop represents each Stop for a [Post]
 */
data class RouteStop(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

/**
 * Function to map Route to [RouteItemUiState]
 */
fun Route.toUi(): RouteItemUiState {
    return RouteItemUiState(
        id = id,
        title = title,
        avgDifficulty = difficulty,
        disabilityFriendly = disabilityFriendly,
        previewPhoto = photos.first(),
        authorId = author.id
    )
}

fun Route.toDetailUi(): RouteUiState {
    return RouteUiState(
        id = id,
        title = title,
        description = description,
        difficulty = when (difficulty) {
            1 -> com.unina.natourkt.presentation.route_details.Difficulty.EASY
            2 -> com.unina.natourkt.presentation.route_details.Difficulty.MEDIUM
            3 -> com.unina.natourkt.presentation.route_details.Difficulty.HARD
            else -> com.unina.natourkt.presentation.route_details.Difficulty.EASY
        },
        duration = duration,
        disabilityFriendly = disabilityFriendly,
        modifiedDate = modifiedDate,
        isReported = isReported,
        photos = photos,
        stops = stops.map {
            RouteStopUiState(
                stopNumber = it.stopNumber,
                latitude = it.latitude,
                longitude = it.longitude
            )
        },
        authorId = author.id
    )
}