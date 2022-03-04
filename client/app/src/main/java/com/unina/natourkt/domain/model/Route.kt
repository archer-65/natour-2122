package com.unina.natourkt.domain.model.route

import android.util.Log
import com.unina.natourkt.data.remote.dto.route.RouteDto
import com.unina.natourkt.data.remote.dto.route.RoutePhotoDto
import com.unina.natourkt.data.remote.dto.route.RouteStopDto
import com.unina.natourkt.domain.model.DirectionsRequest
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.model.toDto
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import java.time.LocalDate

/**
 * Route model (to improve)
 */
data class Route(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val avgDifficulty: Int = 0,
    val avgDuration: Double = 1.0,
    val disabledFriendly: Boolean = false,
    val creationDate: LocalDate? = null,
    val modifiedDate: LocalDate? = null,
    val photos: List<String> = emptyList(),
    val stops: List<RouteStop> = emptyList(),
    val user: User? = null
)

/**
 * RouteStop represents each Stop for a [Post]
 */
data class RouteStop(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
)

/**
 * Function to map Route to [RouteItemUiState]
 */
fun Route.toUi(): RouteItemUiState {
    return RouteItemUiState(
        id = id,
        title = title,
        avgDifficulty = avgDifficulty,
        disabledFriendly = disabledFriendly,
        previewPhoto = photos.first()
    )
}

fun Route.toDto(): RouteDto {
    return RouteDto(
        id = id,
        title = title,
        description = description,
        avgDifficulty = avgDifficulty,
        avgDuration = avgDuration,
        disabledFriendly = disabledFriendly,
        photos = photos.map { it.toPhoto() },
        stops = stops.map { it.toDto() },
        user = user!!.toDto()
    )
}

fun RouteStop.toDto(): RouteStopDto {
    return RouteStopDto(
        id = null,
        stopNumber = stopNumber,
        latitude = latitude,
        longitude = longitude
    )
}

fun String.toPhoto(): RoutePhotoDto {
    return RoutePhotoDto(
        id = null,
        photo = this
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