package com.unina.natourkt.domain.model.route

import android.util.Log
import com.unina.natourkt.domain.model.DirectionsRequest
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import java.time.LocalDate

/**
 * Route model (to improve)
 */
data class Route(
    val id: Long,
    val title: String,
    val description: String? = null,
    val avgDifficulty: Int = 0,
    val avgDuration: Double? = null,
    val disabledFriendly: Boolean = false,
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
        val way = middle.joinToString(separator = "%7C", postfix = "") { "${it.latitude},${it.longitude}" }
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