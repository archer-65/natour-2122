package com.unina.natourkt.presentation.new_route

import android.graphics.Bitmap
import com.unina.natourkt.common.DataState

data class NewRouteUiState(
    val isLoading: Boolean = false,
    val errorMessages: DataState.CustomMessages? = null,
    val routeInfo: NewRouteInfo = NewRouteInfo(),
    val routeStops: List<NewRouteStop> = emptyList(),
    val routePhotos: List<Bitmap> = emptyList()
)

data class NewRouteInfo(
    val routeTitle: String = "",
    val routeDescription: String = "",
    val duration: Int = 1,
    val disabilityFriendly: Boolean = false,
    val difficulty: Difficulty = Difficulty.EASY,
//    val stops: List<NewRouteStop> = emptyList(),
//    val photos: List<Bitmap> = emptyList()
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
