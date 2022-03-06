package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.domain.model.RouteTitle

data class RouteTitleDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,
)

fun RouteTitleDto.toRouteTitle(): RouteTitle {
    return RouteTitle(
        id = id,
        title = title,
    )
}

