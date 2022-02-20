package com.unina.natourkt.domain.model.route

import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.routes.RouteItemUiState
import java.time.LocalDate

/**
 * Route model (to improve)
 */
data class Route(
    val id: Long,
    val title: String,
    val description: String? = null,
    val avgDifficulty: Int = 0,
    val avgDuration: Double? = null,
    val disabledFriendly: Boolean = false,
    val modifiedDate: LocalDate? = null,
    val photos: List<String> = emptyList(),
    val stops: List<RouteStop> = emptyList(),
    val user: User? = null
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