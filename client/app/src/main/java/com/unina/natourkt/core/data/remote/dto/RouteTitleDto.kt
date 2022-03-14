package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteTitleDto(
    @SerializedName("route_id")
    val id: Long,

    @SerializedName("route_title")
    val title: String,
)

