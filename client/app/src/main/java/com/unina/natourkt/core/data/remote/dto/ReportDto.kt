package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReportDto(
    @SerializedName("report_id")
    val reportId: Long,

    @SerializedName("report_title")
    val reportTitle: String,

    @SerializedName("report_description")
    val reportDescription: String,

    @SerializedName("author")
    val author: UserDto,

    @SerializedName("reported_route")
    val reportedRoute: RouteTitleDto
)