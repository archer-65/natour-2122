package com.unina.natourkt.core.domain.use_case.maps

import android.util.Log
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.DirectionsPolyline
import com.unina.natourkt.core.domain.model.RouteStop
import com.unina.natourkt.core.domain.repository.MapsRepository
import com.unina.natourkt.core.domain.util.toDirectionsRequest
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