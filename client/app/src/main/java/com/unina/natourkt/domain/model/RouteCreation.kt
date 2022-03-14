package com.unina.natourkt.domain.model

import android.util.Log
import com.unina.natourkt.data.remote.dto.RouteCreationDto
import com.unina.natourkt.data.remote.dto.RouteStopDto
import com.unina.natourkt.domain.model.route.RouteStop

data class RouteCreation(
    val title: String,
    val description: String,
    val avgDifficulty: Int,
    val avgDuration: Double,
    val disabilityFriendly: Boolean,
    val photos: List<String>,
    val stops: List<RouteStop>,
    val author: User?,
)

data class RouteStopCreation(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

fun RouteCreation.toCreationDto(): RouteCreationDto {
    return RouteCreationDto(
        title = title,
        description = description,
        difficulty = avgDifficulty,
        duration = avgDuration,
        isDisabilityFriendly = disabilityFriendly,
        photos = photos.map {
            RouteCreationDto.Photo(
                it
            )
        },
        stops = stops.map { it.toDto() },
        author = author!!.toDto()
    )
}

fun RouteStop.toDto(): RouteStopDto {
    return RouteStopDto(
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude
    )
}

fun List<RouteStop>.toDirectionsRequest(): DirectionsRequest {
    val first = this.first()
    val origin = "${first.latitude},${first.longitude}"

    val last = this.last()
    val destination = "${last.latitude},${last.longitude}"

    // When the List of RouteStop have size greater than 2
    val waypoints = if (this.size > 2) {
        // Slice from second to last - 1
        val middle = this.slice(1..(this.size - 1))
        // Make a string of this format: "latitude1,longitude1%7Clatitude2,longitude2"
        val way = middle.joinToString(
            separator = "%7C",
            postfix = ""
        ) { "${it.latitude},${it.longitude}" }
        Log.i("WAY", way)
        way
    } else {
        ""
    }

    return DirectionsRequest(
        origin,
        destination,
        waypoints
    )
}