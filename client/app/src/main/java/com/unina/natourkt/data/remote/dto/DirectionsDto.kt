package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DirectionsDto(
    @SerializedName("routes")
    val routes: List<Route>,

    @SerializedName("status")
    val status: String,
) {

    data class Route(
        @SerializedName("overview_polyline")
        val overviewPolyline: OverviewPolyline
    ) {

        data class OverviewPolyline(
            @SerializedName("points")
            val points: String
        )
    }
}
