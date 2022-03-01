package com.unina.natourkt.data.repository

import android.util.Log
import com.unina.natourkt.data.remote.dto.toDirectionsPolyline
import com.unina.natourkt.data.remote.retrofit.MapsApi
import com.unina.natourkt.domain.model.DirectionsRequest
import com.unina.natourkt.domain.model.DirectionsPolyline
import com.unina.natourkt.domain.repository.MapsRepository
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    private val mapsApi: MapsApi,
) : MapsRepository {

    override suspend fun getDirections(
        directionsRequest: DirectionsRequest
    ): DirectionsPolyline {

        return mapsApi.getDirections(
            origin = directionsRequest.origin,
            destination = directionsRequest.destination,
            waypoints = directionsRequest.waypoints
        ).toDirectionsPolyline()
    }
}