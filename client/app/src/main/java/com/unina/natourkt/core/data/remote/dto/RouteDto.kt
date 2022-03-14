package com.unina.natourkt.core.data.remote.dto.route

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.core.data.remote.dto.RouteStopDto
import com.unina.natourkt.core.data.remote.dto.UserDto
import com.unina.natourkt.core.domain.model.route.Route

/**
 * This class represents the response from API for [Route]
 */
data class RouteDto(
    @SerializedName("route_id")
    val id: Long,

    @SerializedName("route_title")
    val title: String,

    @SerializedName("route_description")
    val description: String?,

    @SerializedName("route_difficulty")
    val difficulty: Int,

    @SerializedName("route_duration")
    val duration: Double,

    @SerializedName("is_disability_friendly")
    val isDisabilityFriendly: Boolean,

    @SerializedName("route_creation_date")
    val creationDate: String,

    @SerializedName("route_modified_date")
    val modifiedDate: String?,

    @SerializedName("is_route_reported")
    val isRouteReported: Boolean,

    @SerializedName("route_photos")
    val photos: List<Photo>,

    @SerializedName("route_stops")
    val stops: List<RouteStopDto>,

    @SerializedName("route_author")
    val author: UserDto,
) {

    data class Photo(
        @SerializedName("id")
        val id: Long,

        @SerializedName("photo")
        val photo: String
    )
}