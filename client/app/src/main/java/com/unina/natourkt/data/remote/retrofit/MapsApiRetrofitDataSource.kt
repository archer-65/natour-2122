package com.unina.natourkt.data.remote.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApiRetrofitDataSource {

    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String = "walking",
        @Query("waypoints") waypoints: String,
        @Query("key") key: String,
    )
}