package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.domain.model.DirectionsPolyline
import com.unina.natourkt.core.domain.model.DirectionsRequest
import com.unina.natourkt.core.util.DataState

/**
 * Interface for map functions repository
 */
interface MapsRepository {

    /**
     * This function gets directions given a [DirectionsRequest], then returns a Polyline to decode
     * to obtain directions on Map
     */
    suspend fun getDirections(
        directionsRequest: DirectionsRequest
    ): DataState<DirectionsPolyline>

}