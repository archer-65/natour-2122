package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.routes.RouteItemUiState
import java.time.LocalDate

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

fun Route.toUi(): RouteItemUiState {
    return RouteItemUiState(
        id = id,
        title = title,
        avgDifficulty = avgDifficulty,
        disabledFriendly = disabledFriendly,
        previewPhoto = photos.first()
    )
}