package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.RatingCreationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RatingApi {

    @POST("/ratings/add")
    suspend fun createRating(
        @Body rating: RatingCreationDto,
    )
}