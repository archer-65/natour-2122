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
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("avgDifficulty")
    val avgDifficulty: Int,

    @SerializedName("avgDuration")
    val avgDuration: Double,

    @SerializedName("disabledFriendly")
    val disabledFriendly: Boolean,

    @SerializedName("creationDate")
    val creationDate: String,

    @SerializedName("modifiedDate")
    val modifiedDate: String?,

    @SerializedName("photos")
    val photos: List<RoutePhotoDto>,

    @SerializedName("stops")
    val stops: List<RouteStopDto>,

    @SerializedName("user")
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
    @SerializedName("id")
    val id: Long,

    @SerializedName("stopNumber")
    val stopNumber: Int,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
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
        avgDifficulty = avgDifficulty,
        avgDuration = avgDuration,
        disabledFriendly = disabledFriendly,
        creationDate = creationDate.toDateTime(),
        modifiedDate = modifiedDate?.toDateTime(),
        photos = photos.map { photo -> photo.photo },
        stops = stops.map { stop -> stop.toRouteStop() },
        user = author.toUser()
    )
}

fun RouteStopDto.toRouteStop(): RouteStop {
    return RouteStop(
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude
    )
}