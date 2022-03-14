package com.unina.natourkt.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.data.remote.dto.mapper.RouteApiMapper
import com.unina.natourkt.core.data.remote.retrofit.RouteApi
import com.unina.natourkt.core.data.repository.RouteRepositoryImpl
import com.unina.natourkt.core.domain.model.route.Route
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class PersonalRoutePagingSource @Inject constructor(
    private val api: RouteApi,
    private val routeApiMapper: RouteApiMapper,
    private val userId: Long
) : PagingSource<Int, Route>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Route> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getRoutesByUser(userId, position, params.loadSize)
            Log.i(Constants.ROUTE_MODEL, "$response")

            LoadResult.Page(
                data = response.map { dto -> routeApiMapper.mapToDomain(dto) },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                // Avoids duplicates
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / RouteRepositoryImpl.NETWORK_PAGE_SIZE)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e(Constants.ROUTE_MODEL, e.localizedMessage ?: "Network error retrieving Routes", e)
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            Log.e(Constants.ROUTE_MODEL, e.localizedMessage ?: "HTTP error retrieving Routes", e)
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Route>): Int {
        return INITIAL_PAGE
    }
}