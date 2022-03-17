package com.unina.natourkt.core.data.repository

import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.data.paging.CompilationRoutePagingSource
import com.unina.natourkt.core.data.paging.FilteredRoutesPagingSource
import com.unina.natourkt.core.data.paging.PersonalRoutePagingSource
import com.unina.natourkt.core.data.paging.RoutePagingSource
import com.unina.natourkt.core.data.remote.dto.mapper.RouteApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.RouteCreationApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.RouteTitleApiMapper
import com.unina.natourkt.core.data.remote.retrofit.RouteApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.Filter
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.domain.repository.StorageRepository
import com.unina.natourkt.core.util.toInputStream
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [RouteRepository] contains [Route] related functions for incoming
 * responses from [RouteApi] and uses [RoutePagingSource] to Paginate with
 * Paging 3 library
 */
class RouteRepositoryImpl @Inject constructor(
    private val api: RouteApi,
    private val routeApiMapper: RouteApiMapper,
    private val routeTitleApiMapper: RouteTitleApiMapper,
    private val routeCreationApiMapper: RouteCreationApiMapper,
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
            pagingSourceFactory = { RoutePagingSource(api, routeApiMapper) }
        ).flow
    }

    override fun getPersonalRoutes(userId: Long): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonalRoutePagingSource(api, routeApiMapper, userId) }
        ).flow
    }

    override fun getCompilationRoutes(compilationId: Long): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CompilationRoutePagingSource(
                    api,
                    routeApiMapper,
                    compilationId
                )
            }
        ).flow
    }

    override fun getFilteredRoutes(filter: Filter): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FilteredRoutesPagingSource(filter, routeApiMapper, api) }
        ).flow
    }

    override suspend fun getRouteTitle(title: String): DataState<List<RouteTitle>> =
        safeApiCall(IO) {
            val titlesResponse = api.getRouteTitles(title)
            titlesResponse.map { routeTitleApiMapper.mapToDomain(it) }
        }


    override suspend fun createRoute(route: RouteCreation): DataState<Unit> =
        safeApiCall(IO) {
            val routeRequest = routeCreationApiMapper.mapToDto(route)
            api.createRoute(routeRequest)
        }

    override suspend fun getRouteById(id: Long): DataState<Route> =
        safeApiCall(IO) {
            val routeResponse = api.getRouteById(id)
            routeApiMapper.mapToDomain(routeResponse)
        }
}