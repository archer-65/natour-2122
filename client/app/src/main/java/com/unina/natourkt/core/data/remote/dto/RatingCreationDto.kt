package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RatingCreationDto(
    @SerializedName("rating_difficulty")
    val ratingDifficulty: Int,

    @SerializedName("rating_duration")
    val ratingDuration: Float,

    @SerializedName("author_id")
    val authorId: Long,

    @SerializedName("rated_route_id")
    val ratedRouteId: Long
)