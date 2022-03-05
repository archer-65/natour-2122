package com.unina.natourkt.domain.repository

import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.DirectionsRequest
import com.unina.natourkt.domain.model.DirectionsPolyline

interface MapsRepository {

    suspend fun getDirections(
        directionsRequest: DirectionsRequest
    ): DataState<DirectionsPolyline>

}