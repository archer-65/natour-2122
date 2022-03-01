package com.unina.natourkt.domain.use_case.maps

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.decodePolyline
import com.unina.natourkt.domain.model.DirectionsPolyline
import com.unina.natourkt.domain.model.route.RouteStop
import com.unina.natourkt.domain.model.route.toDirectionsRequest
import com.unina.natourkt.domain.repository.MapsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDirectionsUseCase @Inject constructor(
    private val mapsRepository: MapsRepository
) {

    operator fun invoke(stops: List<RouteStop>): Flow<DataState<DirectionsPolyline>> = flow {
        try {
            emit(DataState.Loading())
            Log.i("DIRECTIONS REQUEST", "Loading request for directions")
            val directionsRequest = stops.toDirectionsRequest()
            Log.i("DIRECTIONS REQUEST", "$directionsRequest")

            val directions = mapsRepository.getDirections(directionsRequest)
            Log.i("DIRECTIONS RESPONSE", directions.toString())
            emit(DataState.Success(directions))
        } catch (e: Exception) {
            Log.e("DIRECTIONS ERROR", e.localizedMessage, e)
            emit(DataState.Error(DataState.CustomMessage.NotAcceptable))
        }
    }
}