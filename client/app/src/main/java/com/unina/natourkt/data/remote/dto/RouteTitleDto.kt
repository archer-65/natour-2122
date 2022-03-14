package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.domain.model.RouteTitle

data class RouteTitleDto(
    @SerializedName("route_id")
    val id: Long,

    @SerializedName("route_title")
    val title: String,
)

