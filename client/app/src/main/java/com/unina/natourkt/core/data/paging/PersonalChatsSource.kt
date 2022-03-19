package com.unina.natourkt.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.core.data.remote.dto.mapper.ChatApiMapper
import com.unina.natourkt.core.data.remote.retrofit.ChatApi
import com.unina.natourkt.core.data.repository.ChatRepositoryImpl
import com.unina.natourkt.core.data.repository.RouteRepositoryImpl
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.util.Constants
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class PersonalChatsSource @Inject constructor(
    private val api: ChatApi,
    private val chatApiMapper: ChatApiMapper,
    private val userId: Long
) : PagingSource<Int, Chat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getChatsByUser(userId, position, params.loadSize)
            Log.i(Constants.ROUTE_MODEL, "$response")

            LoadResult.Page(
                data = response.map { dto -> chatApiMapper.mapToDomain(dto) },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                // Avoids duplicates
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / ChatRepositoryImpl.NETWORK_PAGE_SIZE)
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

    override fun getRefreshKey(state: PagingState<Int, Chat>): Int {
        return INITIAL_PAGE
    }
}