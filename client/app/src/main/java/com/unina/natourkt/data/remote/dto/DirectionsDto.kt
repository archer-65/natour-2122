package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.common.decodePolyline
import com.unina.natourkt.domain.model.DirectionsPolyline

data class DirectionsDto(
    @SerializedName("routes")
    val routes: List<DirectionsRoute>,

    @SerializedName("status")
    val status: String,
)

data class DirectionsRoute(
    @SerializedName("overview_polyline")
    val overviewPolyline: DirectionsOverviewPolyline
)

data class DirectionsOverviewPolyline(
    @SerializedName("points")
    val points: String
)

fun DirectionsDto.toDirectionsPolyline(): DirectionsPolyline {
    return DirectionsPolyline(
        points = routes.flatMap {
            it.overviewPolyline.points.decodePolyline()
        }
    )
}
