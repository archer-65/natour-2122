package com.unina.natourkt.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.data.remote.PostRetrofitDataSource
import com.unina.natourkt.data.remote.dto.toPost
import com.unina.natourkt.data.repository.PostRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.domain.model.post.Post
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class PostPagingSource @Inject constructor(
    private val retrofitDataSource: PostRetrofitDataSource,
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = retrofitDataSource.getPosts(position, params.loadSize)
            Log.i("Response", "$response")

            LoadResult.Page(
                data = response.map { postDto -> postDto.toPost() },
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

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}