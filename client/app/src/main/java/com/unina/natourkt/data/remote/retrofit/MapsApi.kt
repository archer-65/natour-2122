package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.DirectionsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {

    @GET("/directions")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String = "walking",
        @Query(value = "waypoints", encoded = true) waypoints: String = "",
    ): DirectionsDto
}