package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.CompilationCreationDto
import com.unina.natourkt.core.data.remote.dto.CompilationDto
import retrofit2.http.*

interface CompilationApi {

    @GET("/compilations/search_page")
    suspend fun getCompilationsByUser(
        @Query("user_id") userId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<CompilationDto>

    @GET("/compilations/search_exclude_route")
    suspend fun getCompilationsByUserAndRouteNotPresent(
        @Query("user_id") userId: Long,
        @Query("route_id") routeId: Long,
    ): List<CompilationDto>

    @POST("/compilations/add")
    suspend fun createCompilation(
        @Body compilation: CompilationCreationDto
    )

    @PUT("/compilations/{id}/add")
    suspend fun addRouteToCompilation(
        @Path(value = "id") compilationId: Long,
        @Query(value = "route_id") routeId: Long,
    )
}