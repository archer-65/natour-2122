package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Filter
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for route related functions
 */
interface RouteRepository {

    /**
     * This function gets all routes
     */
    fun getRoutes(): Flow<PagingData<Route>>

    /**
     * This function gets all routes for logged user
     */
    fun getPersonalRoutes(userId: Long): Flow<PagingData<Route>>

    /**
     * This function gets all filtered routes given a [Filter] object
     */
    fun getFilteredRoutes(filter: Filter): Flow<PagingData<Route>>

    /**
     * This function gets all routes contained in a certain compilation
     */
    fun getCompilationRoutes(compilationId: Long): Flow<PagingData<Route>>

    /**
     * This function creates a route taking only a [RouteCreation] model as
     * parameter
     */
    suspend fun createRoute(route: RouteCreation): DataState<Unit>

    /**
     * This function gets all routes titles containing a keyword
     */
    suspend fun getRoutesTitles(title: String): DataState<List<RouteTitle>>

    /**
     * This function gets specific route through its id
     */
    suspend fun getRouteById(routeId: Long): DataState<Route>

    /**
     * This function deletes a route given a certain routeId
     */
    suspend fun deleteRoute(routeId: Long): DataState<Unit>

    /**
     * This function updates a route after modifications to the original one
     */
    suspend fun updateRoute(route: Route): DataState<Unit>
}