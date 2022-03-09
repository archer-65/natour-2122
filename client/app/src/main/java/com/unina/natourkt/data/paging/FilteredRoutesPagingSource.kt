package com.unina.natourkt.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.common.Constants
import com.unina.natourkt.data.remote.dto.route.toRoute
import com.unina.natourkt.data.remote.retrofit.RouteApi
import com.unina.natourkt.data.repository.RouteRepositoryImpl
import com.unina.natourkt.domain.model.Filter
import com.unina.natourkt.domain.model.route.Route
import retrofit2.HttpException
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class FilteredRoutesPagingSource @Inject constructor(
    private val filter: Filter,
    private val api: RouteApi
) : PagingSource<Int, Route>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Route> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getRoutesByFilter(
                query = filter.query,
                difficulty = filter.minDifficulty,
                minDuration = filter.minDuration?.toFloat(),
                maxDuration = filter.maxDuration?.toFloat(),
                latitude = filter.latitude,
                longitude = filter.longitude,
                distance = filter.distance,
                isDisabilityFriendly = filter.isDisabilityFriendly,
                pageNo = position,
                pageSize = params.loadSize
            )

            Log.i(Constants.ROUTE_MODEL, "$response")

            LoadResult.Page(
                data = response.map { routeDto -> routeDto.toRoute() },
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