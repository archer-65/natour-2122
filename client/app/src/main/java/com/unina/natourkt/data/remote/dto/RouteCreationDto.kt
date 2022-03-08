package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteCreationDto(
    @SerializedName("route_title")
    val title: String,

    @SerializedName("route_description")
    val description: String,

    @SerializedName("route_difficulty")
    val difficulty: Int,

    @SerializedName("route_duration")
    val duration: Double,

    @SerializedName("is_disability_friendly")
    val isDisabilityFriendly: Boolean,

    @SerializedName("route_photos")
    val photos: List<RoutePhotoCreationDto>,

    @SerializedName("route_stops")
    val stops: List<RouteStopCreationDto>,

    @SerializedName("route_author")
    val author: UserDto,
)

data class RoutePhotoCreationDto(
    @SerializedName("photo")
    val photo: String
)

data class RouteStopCreationDto(
    @SerializedName("stop_number")
    val stopNumber: Int,

    @SerializedName("stop_latitude")
    val latitude: Double,

    @SerializedName("stop_longitude")
    val longitude: Double,
)
