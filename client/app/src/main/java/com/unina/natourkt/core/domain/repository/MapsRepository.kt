package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.DirectionsRequest
import com.unina.natourkt.core.domain.model.DirectionsPolyline

interface MapsRepository {

    suspend fun getDirections(
        directionsRequest: DirectionsRequest
    ): DataState<DirectionsPolyline>

}