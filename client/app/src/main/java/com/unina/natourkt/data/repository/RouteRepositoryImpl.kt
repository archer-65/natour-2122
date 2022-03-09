package com.unina.natourkt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.paging.FilteredRoutesPagingSource
import com.unina.natourkt.data.paging.PersonalRoutePagingSource
import com.unina.natourkt.data.paging.RoutePagingSource
import com.unina.natourkt.data.remote.dto.route.toRoute
import com.unina.natourkt.data.remote.dto.toRouteTitle
import com.unina.natourkt.data.remote.retrofit.RouteApi
import com.unina.natourkt.data.util.safeApiCall
import com.unina.natourkt.domain.model.Filter
import com.unina.natourkt.domain.model.RouteCreation
import com.unina.natourkt.domain.model.RouteTitle
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.model.toCreationDto
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [RouteRepository] contains [Route] related functions for incoming
 * responses from [RouteApi] and uses [RoutePagingSource] to Paginate with
 * Paging 3 library
 */
class RouteRepositoryImpl @Inject constructor(
    private val api: RouteApi
) : RouteRepository {

    /**
     * Page size for network requests for this class
     * NOTE: The first page defaults to pageSize * 3!
     */
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    /**
     * This functions return paginated data for [Route] as a flow
     */
    override fun getRoutes(): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RoutePagingSource(api) }
        ).flow
    }

    override fun getPersonalRoutes(userId: Long): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonalRoutePagingSource(api, userId) }
        ).flow
    }

    override fun getFilteredRoutes(filter: Filter): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FilteredRoutesPagingSource(filter, api) }
        ).flow
    }

    override suspend fun getRouteTitle(title: String): DataState<List<RouteTitle>> =
        safeApiCall(IO) {
            api.getRouteTitles(title).map {
                it.toRouteTitle()
            }
        }


    override suspend fun createRoute(route: RouteCreation): DataState<Unit> =
        safeApiCall(IO) {
            api.createRoute(route.toCreationDto())
        }

    override suspend fun getRouteById(id: Long): DataState<Route> =
        safeApiCall(IO) {
            api.getRouteById(id).toRoute()
        }
}