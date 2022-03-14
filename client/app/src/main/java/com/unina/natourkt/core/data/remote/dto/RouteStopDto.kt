package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteStopDto(
    @SerializedName("stop_number")
    val stopNumber: Int,

    @SerializedName("stop_latitude")
    val latitude: Double,

    @SerializedName("stop_longitude")
    val longitude: Double,
)
