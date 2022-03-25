package com.unina.natourkt.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.core.data.remote.dto.mapper.UserApiMapper
import com.unina.natourkt.core.data.remote.retrofit.UserApi
import com.unina.natourkt.core.data.repository.UserRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.util.Constants.ROUTE_MODEL
import com.unina.natourkt.core.util.Constants.USER_MODEL
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class UsersSource @Inject constructor(
    private val api: UserApi,
    private val userApiMapper: UserApiMapper,
    private val query: String,
    private val loggedUser: Long,
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getUsersByName(query, loggedUser, position, params.loadSize)
            Log.i(USER_MODEL, "$response")

            LoadResult.Page(
                data = response.map { dto -> userApiMapper.mapToDomain(dto) },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                // Avoids duplicates
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / NETWORK_PAGE_SIZE)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e(USER_MODEL, e.localizedMessage ?: "Network error retrieving Routes", e)
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            Log.e(USER_MODEL, e.localizedMessage ?: "HTTP error retrieving Routes", e)
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int {
        return INITIAL_PAGE
    }
}