package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.route.RouteDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for [RouteDto]
 */
interface RouteRetrofitDataSource {

    /**
     * Classic Get all routes (paginated)
     */
    @GET("/routes")
    suspend fun getRoutes(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ): List<RouteDto>

    /**
     * Get all routes by user id (paginated)
     */
    @GET("/routes/search_page")
    suspend fun getRoutesByUser(
        @Query("userId") userId: Long,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ) : List<RouteDto>
}