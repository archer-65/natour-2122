package com.unina.natourkt.feature_route.create_route.map

import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.core.presentation.model.RouteStopUi

data class CreateRouteMapUiState(
    val isLoadedFromGPX: Boolean = false,
    val stops: List<RouteStopUi> = emptyList(),
    val polylineOptions: PolylineOptions = PolylineOptions()
) {

    val isButtonEnabled: Boolean
        get() = stops.size >= 1

    val shouldGetDirections: Boolean
        get() = stops.size >= 2
}