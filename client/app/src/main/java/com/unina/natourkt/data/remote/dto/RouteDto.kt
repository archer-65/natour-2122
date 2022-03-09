package com.unina.natourkt.data.remote.dto.route

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.common.toDateTime
import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.model.route.RouteStop

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
    val photos: List<RoutePhotoDto>,

    @SerializedName("route_stops")
    val stops: List<RouteStopDto>,

    @SerializedName("route_author")
    val author: UserDto,
)

/**
 * Only contains the response for route's photos
 */
data class RoutePhotoDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("photo")
    val photo: String
)

data class RouteStopDto(
    @SerializedName("stop_id")
    val id: Long,

    @SerializedName("stop_number")
    val stopNumber: Int,

    @SerializedName("stop_latitude")
    val latitude: Double,

    @SerializedName("stop_longitude")
    val longitude: Double,
)

/**
 * Function to map [RouteDto] to [Route]
 */
fun RouteDto.toRoute(): Route {
    return Route(
        id = id,
        title = title,
        description = description ?: "",
        difficulty = difficulty,
        duration = duration,
        disabilityFriendly = isDisabilityFriendly,
        creationDate = creationDate.toDateTime(),
        modifiedDate = modifiedDate?.toDateTime(),
        isReported = isRouteReported,
        photos = photos.map { photo -> photo.photo },
        stops = stops.map { stop -> stop.toRouteStop() },
        author = author.toUser()
    )
}

fun RouteStopDto.toRouteStop(): RouteStop {
    return RouteStop(
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude
    )
}