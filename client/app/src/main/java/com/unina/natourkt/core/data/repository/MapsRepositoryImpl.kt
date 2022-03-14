package com.unina.natourkt.core.data.repository

import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.data.remote.dto.mapper.DirectionsApiMapper
import com.unina.natourkt.core.data.remote.retrofit.MapsApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.DirectionsPolyline
import com.unina.natourkt.core.domain.model.DirectionsRequest
import com.unina.natourkt.core.domain.repository.MapsRepository
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    private val mapsApi: MapsApi,
    private val directionsApiMapper: DirectionsApiMapper
) : MapsRepository {

    override suspend fun getDirections(
        directionsRequest: DirectionsRequest
    ): DataState<DirectionsPolyline> {

        return safeApiCall(IO) {
            val directionsResponse = mapsApi.getDirections(
                origin = directionsRequest.origin,
                destination = directionsRequest.destination,
                waypoints = directionsRequest.waypoints
            )
            directionsApiMapper.mapToDomain(directionsResponse)
        }
    }
}