package com.unina.natourkt.core.domain.util

import android.util.Log
import com.unina.natourkt.core.domain.model.DirectionsRequest
import com.unina.natourkt.core.domain.model.RouteStop

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