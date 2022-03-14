package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.Filter
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.domain.model.route.Route
import kotlinx.coroutines.flow.Flow

/**
 * Interface for route related functions
 */
interface RouteRepository {

    /**
     * Get paginated routes
     */
    fun getRoutes(): Flow<PagingData<Route>>

    /**
     * Get paginated routes for logged user
     */
    fun getPersonalRoutes(userId: Long): Flow<PagingData<Route>>

    fun getFilteredRoutes(filter: Filter): Flow<PagingData<Route>>

    suspend fun createRoute(route: RouteCreation): DataState<Unit>

    suspend fun getRouteTitle(title: String): DataState<List<RouteTitle>>

    suspend fun getRouteById(id: Long): DataState<Route>
    fun getCompilationRoutes(compilationId: Long): Flow<PagingData<Route>>
}