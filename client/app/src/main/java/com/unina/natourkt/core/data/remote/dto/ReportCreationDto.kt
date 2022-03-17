package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReportCreationDto(
    @SerializedName("report_title")
    val reportTitle: String,

    @SerializedName("report_description")
    val reportDescription: String,

    @SerializedName("author_id")
    val authorId: Long,

    @SerializedName("reported_route_id")
    val reportedRouteId: Long
)