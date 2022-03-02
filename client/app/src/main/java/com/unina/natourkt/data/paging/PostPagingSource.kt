package com.unina.natourkt.data.paging

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.common.Constants.POST_MODEL
import com.unina.natourkt.common.NetworkConnectionInterceptor
import com.unina.natourkt.data.remote.dto.post.toPost
import com.unina.natourkt.data.remote.retrofit.PostApi
import com.unina.natourkt.data.repository.PostRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.domain.model.Post
import retrofit2.HttpException
import java.io.IOException

private const val INITIAL_PAGE = 0

class PostPagingSource(
    private val api: PostApi,
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getPosts(position, params.loadSize)
            Log.i(POST_MODEL, "$response")

            LoadResult.Page(
                data = response.map { postDto -> postDto.toPost() },
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