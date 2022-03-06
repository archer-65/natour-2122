package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteCreationDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("avgDifficulty")
    val avgDifficulty: Int,

    @SerializedName("avgDuration")
    val avgDuration: Double,

    @SerializedName("disabledFriendly")
    val disabledFriendly: Boolean,

    @SerializedName("photos")
    val photos: List<RoutePhotoCreationDto>,

    @SerializedName("stops")
    val stops: List<RouteStopCreationDto>,

    @SerializedName("user")
    val author: UserDto,
)

data class RoutePhotoCreationDto(
    @SerializedName("photo")
    val photo: String
)

data class RouteStopCreationDto(
    @SerializedName("stopNumber")
    val stopNumber: Int,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,
)
