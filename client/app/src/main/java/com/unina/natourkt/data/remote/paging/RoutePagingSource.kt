package com.unina.natourkt.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.data.remote.dto.route.toRoute
import com.unina.natourkt.data.remote.retrofit.RouteRetrofitDataSource
import com.unina.natourkt.data.repository.RouteRepositoryImpl
import com.unina.natourkt.data.repository.RouteRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.domain.model.Route
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class RoutePagingSource @Inject constructor(
    private val retrofitDataSource: RouteRetrofitDataSource
) : PagingSource<Int, Route>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Route> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = retrofitDataSource.getRoutes(position, params.loadSize)
            Log.i("Response", "$response")

            LoadResult.Page(
                data = response.map { routeDto -> routeDto.toRoute() },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / NETWORK_PAGE_SIZE)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Route>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}