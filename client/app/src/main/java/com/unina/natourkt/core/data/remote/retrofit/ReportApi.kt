package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.ReportCreationDto
import com.unina.natourkt.core.data.remote.dto.ReportDto
import com.unina.natourkt.core.data.remote.dto.route.RouteDto
import retrofit2.http.*

interface ReportApi {

    @POST("/reports/add")
    suspend fun createReport(
        @Body report: ReportCreationDto,
    )

    @GET("/reports/page")
    suspend fun getReports(
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<ReportDto>

    @DELETE("/reports/{id}/delete")
    suspend fun deleteReport(
        @Path("id") reportId: Long,
    )
}