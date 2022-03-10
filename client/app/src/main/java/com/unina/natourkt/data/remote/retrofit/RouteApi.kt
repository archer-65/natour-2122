package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.RouteCreationDto
import com.unina.natourkt.data.remote.dto.RouteTitleDto
import com.unina.natourkt.data.remote.dto.route.RouteDto
import retrofit2.http.*

/**
 * Retrofit interface for [RouteDto]
 */
interface RouteApi {

    @GET("/routes/{id}")
    suspend fun getRouteById(
        @Path("id") id: Long,
    ): RouteDto

    /**
     * Classic Get all routes (paginated)
     */
    @GET("/routes")
    suspend fun getRoutes(
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<RouteDto>

    /**
     * Get all routes by user id (paginated)
     */
    @GET("/routes/search_page")
    suspend fun getRoutesByUser(
        @Query("user_id") userId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<RouteDto>

    @GET("/routes/compilation")
    suspend fun getRoutesByCompilation(
        @Query("compilation_id") compilationId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<RouteDto>

    @POST("/routes/add")
    suspend fun createRoute(
        @Body routeDto: RouteCreationDto
    )

    @GET("/routes/search_title")
    suspend fun getRouteTitles(
        @Query("query") query: String
    ): List<RouteTitleDto>

    @GET("/routes/filter")
    suspend fun getRoutesByFilter(
        @Query("query") query: String,
        @Query("difficulty") difficulty: Int? = null,
        @Query("min_duration") minDuration: Float? = null,
        @Query("max_duration") maxDuration: Float? = null,
        @Query("disability_friendly") isDisabilityFriendly: Boolean? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("distance") distance: Float? = null,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<RouteDto>
}