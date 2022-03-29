package com.unina.natourkt.core.data.repository

import com.unina.natourkt.core.data.remote.dto.mapper.DirectionsApiMapper
import com.unina.natourkt.core.data.remote.retrofit.MapsApi
import com.unina.natourkt.core.data.util.retrofitSafeCall
import com.unina.natourkt.core.domain.model.DirectionsPolyline
import com.unina.natourkt.core.domain.model.DirectionsRequest
import com.unina.natourkt.core.domain.repository.MapsRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

/**
 * This implementation of [MapsRepository] works with a [MapsApi] Retrofit interface
 * It also contains mapper for model conversions
 * @see [DirectionsApiMapper]
 */
class MapsRepositoryImpl @Inject constructor(
    private val api: MapsApi,
    private val directionsApiMapper: DirectionsApiMapper
) : MapsRepository {

    override suspend fun getDirections(
        directionsRequest: DirectionsRequest
    ): DataState<DirectionsPolyline> {

        return retrofitSafeCall(IO) {
            val directionsResponse = api.getDirections(
                origin = directionsRequest.origin,
                destination = directionsRequest.destination,
                waypoints = directionsRequest.waypoints
            )
            directionsApiMapper.mapToDomain(directionsResponse)
        }
    }
}