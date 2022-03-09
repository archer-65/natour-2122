package com.unina.natourkt.presentation.route_details

import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.model.route.RouteStop
import com.unina.natourkt.presentation.base.ui_state.UserUiState
import com.unina.natourkt.presentation.post_details.PostUiState
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
    val authorId: Long
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

suspend fun RouteUiState.convertKeys(execute: suspend (string: String) -> String): RouteUiState {
    val photos = this.photos.map {
        execute(it)
    }

    return this.copy(photos = photos)
}