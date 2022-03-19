package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReportCreationDto(
    @SerializedName("report_title")
    val reportTitle: String,

    @SerializedName("report_description")
    val reportDescription: String,

    @SerializedName("author")
    val author: Author,

    @SerializedName("reported_route")
    val reportedRoute: ReportedRoute
) {

    data class Author(
        @SerializedName("user_id")
        val id: Long,
    )

    data class ReportedRoute(
        @SerializedName("route_id")
        val id: Long,
    )
}