package com.unina.natourkt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.data.remote.paging.PersonalRoutePagingSource
import com.unina.natourkt.data.remote.paging.RoutePagingSource
import com.unina.natourkt.data.remote.retrofit.RouteRetrofitDataSource
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [RouteRepository] contains [Route] related functions for incoming
 * responses from [RouteRetrofitDataSource] and uses [RoutePagingSource] to Paginate with
 * Paging 3 library
 */
class RouteRepositoryImpl @Inject constructor(
    private val retrofitDataSource: RouteRetrofitDataSource
): RouteRepository {

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
            pagingSourceFactory = { RoutePagingSource(retrofitDataSource) }
        ).flow
    }

    override fun getPersonalRoutes(userId: Long): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonalRoutePagingSource(retrofitDataSource, userId) }
        ).flow
    }
}