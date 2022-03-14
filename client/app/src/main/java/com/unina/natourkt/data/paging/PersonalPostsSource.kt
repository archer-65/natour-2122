package com.unina.natourkt.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.common.Constants.POST_MODEL
import com.unina.natourkt.data.remote.dto.mapper.PostApiMapper
import com.unina.natourkt.data.remote.retrofit.PostApi
import com.unina.natourkt.data.repository.PostRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.domain.model.Post
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class PersonalPostPagingSource @Inject constructor(
    private val api: PostApi,
    private val postApiMapper: PostApiMapper,
    private val userId: Long
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getPostsByUser(userId, position, params.loadSize)
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