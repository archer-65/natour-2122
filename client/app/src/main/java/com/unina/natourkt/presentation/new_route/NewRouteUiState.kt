package com.unina.natourkt.presentation.new_route

import android.graphics.Bitmap
import com.unina.natourkt.common.DataState

data class NewRouteUiState(
    val isLoading: Boolean = false,
    val errorMessages: DataState.CustomMessages? = null,
    val route: NewRoute? = null
)

data class NewRoute(
    val routeTitle: String,
    val routeDescription: String,
    val duration: Int,
    val disabilityFriendly: Boolean = false,
    val difficulty: Int,
    val stops: MutableList<NewRouteStops> = mutableListOf(),
    val photos: MutableList<Bitmap> = mutableListOf()
)

data class NewRouteStops(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

