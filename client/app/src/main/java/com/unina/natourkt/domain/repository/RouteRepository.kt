package com.unina.natourkt.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.route.Route
import kotlinx.coroutines.flow.Flow

/**
 * Interface for route related functions
 */
interface RouteRepository {

    /**
     * Get paginated routes
     */
    fun getRoutes(): Flow<PagingData<Route>>
}