package com.unina.natourkt.domain.model.route

import android.util.Log
import com.unina.natourkt.domain.model.DirectionsRequest
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import com.unina.natourkt.presentation.new_route.Difficulty
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
        previewPhoto = photos.first()
    )
}