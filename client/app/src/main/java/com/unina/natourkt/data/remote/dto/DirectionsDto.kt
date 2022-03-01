package com.unina.natourkt.data.remote.dto

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.unina.natourkt.common.decodePolyline
import com.unina.natourkt.domain.model.DirectionsPolyline

data class DirectionsDto(
    val routes: List<DirectionsRoute>,
    val status: String,
)

data class DirectionsRoute(
    @SerializedName("overview_polyline")
    val overviewPolyline: DirectionsOverviewPolyline
)

data class DirectionsOverviewPolyline(
    val points: String
)

fun DirectionsDto.toDirectionsPolyline(): DirectionsPolyline {
    return DirectionsPolyline(
        points = routes.flatMap {
            it.overviewPolyline.points.decodePolyline() }
    )
}
