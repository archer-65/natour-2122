package com.unina.natourkt.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.core.data.remote.dto.mapper.PostApiMapper
import com.unina.natourkt.core.data.remote.retrofit.PostApi
import com.unina.natourkt.core.data.repository.PostRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.util.Constants.POST_MODEL
import retrofit2.HttpException
import java.io.IOException

private const val INITIAL_PAGE = 0

class PostSource(
    private val api: PostApi,
    private val postApiMapper: PostApiMapper,
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getPosts(position, params.loadSize)
            Log.i(POST_MODEL, "$response")

            LoadResult.Page(
                data = response.map { dto -> postApiMapper.mapToDomain(dto) },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                // Avoids duplicates
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / NETWORK_PAGE_SIZE)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e(POST_MODEL, e.localizedMessage ?: "Network error retrieving Posts", e)
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            Log.e(POST_MODEL, e.localizedMessage ?: "HTTP error retrieving Posts", e)
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int {
        return INITIAL_PAGE
    }
}