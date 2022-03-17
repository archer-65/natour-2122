package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.ReportCreationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {

    @POST("/reports/add")
    suspend fun createReport(
        @Body report: ReportCreationDto,
    )
}