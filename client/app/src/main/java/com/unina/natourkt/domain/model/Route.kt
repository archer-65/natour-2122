package com.unina.natourkt.domain.model.route

import android.util.Log
import com.unina.natourkt.domain.model.DirectionsRequest
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import java.time.LocalDateTime

/**
 * Route model (to improve)
 */
data class Route(
    val id: Long,
    val title: String,
    val description: String,
    val avgDifficulty: Int,
    val avgDuration: Double,
    val disabledFriendly: Boolean,
    val creationDate: LocalDateTime,
    val modifiedDate: LocalDateTime?,
    val photos: List<String>,
    val stops: List<RouteStop>,
    val user: User
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
        avgDifficulty = avgDifficulty,
        disabledFriendly = disabledFriendly,
        previewPhoto = photos.first()
    )
}