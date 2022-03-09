package com.unina.natourkt.domain.use_case.maps

import android.util.Log
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.DirectionsPolyline
import com.unina.natourkt.domain.model.RouteStopCreation
import com.unina.natourkt.domain.model.route.RouteStop
import com.unina.natourkt.domain.model.toDirectionsRequest
import com.unina.natourkt.domain.repository.MapsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDirectionsUseCase @Inject constructor(
    private val mapsRepository: MapsRepository
) {

    operator fun invoke(stops: List<RouteStop>): Flow<DataState<DirectionsPolyline>> = flow {
        Log.i("DIRECTIONS REQUEST", "Loading request for directions")
        emit(DataState.Loading())

        val directionsRequest = stops.toDirectionsRequest()
        Log.i("DIRECTIONS REQUEST", "$directionsRequest")

        val directions = mapsRepository.getDirections(directionsRequest)
        Log.i("DIRECTIONS RESPONSE", directions.toString())
        emit(directions)
    }
}