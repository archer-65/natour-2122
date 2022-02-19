package com.unina.natourkt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.data.remote.paging.RoutePagingSource
import com.unina.natourkt.data.remote.retrofit.RouteRetrofitDataSource
import com.unina.natourkt.domain.model.Route
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val retrofitDataSource: RouteRetrofitDataSource
): RouteRepository {

    override fun getRoutes(): Flow<PagingData<Route>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RoutePagingSource(retrofitDataSource) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}