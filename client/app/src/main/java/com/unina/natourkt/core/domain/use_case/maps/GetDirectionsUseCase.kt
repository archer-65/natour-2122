package com.unina.natourkt.core.domain.use_case.maps

import android.util.Log
import com.unina.natourkt.core.domain.model.DirectionsPolyline
import com.unina.natourkt.core.domain.model.RouteStop
import com.unina.natourkt.core.domain.repository.MapsRepository
import com.unina.natourkt.core.domain.util.toDirectionsRequest
import com.unina.natourkt.core.util.Constants.MAPS
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve directions given multiple [RouteStop] objects
 * @see [MapsRepository]
 */
class GetDirectionsUseCase @Inject constructor(
    private val mapsRepository: MapsRepository
) {

    operator fun invoke(stops: List<RouteStop>): Flow<DataState<DirectionsPolyline>> = flow {
        emit(DataState.Loading())
        Log.i(MAPS, "Loading request for directions")

        val directionsRequest = stops.toDirectionsRequest()
        Log.i(MAPS, "Conversion from stops to directions request: ${directionsRequest}")

        val directions = mapsRepository.getDirections(directionsRequest)
        Log.i(MAPS, "Directions obtained: $directions")

        emit(directions)
    }
}