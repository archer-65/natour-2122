package com.unina.natourkt.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.common.Constants
import com.unina.natourkt.common.Constants.COMPILATION_MODEL
import com.unina.natourkt.data.remote.dto.toCompilation
import com.unina.natourkt.data.remote.retrofit.CompilationRetrofitDataSource
import com.unina.natourkt.data.repository.CompilationRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.unina.natourkt.data.repository.RouteRepositoryImpl
import com.unina.natourkt.domain.model.Compilation
import com.unina.natourkt.domain.model.Post
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class PersonalCompilationPagingSource @Inject constructor(
    private val retrofitDataSource: CompilationRetrofitDataSource,
    private val userId: Long
) : PagingSource<Int, Compilation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Compilation> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response =
                retrofitDataSource.getCompilationsByUser(userId, position, params.loadSize)
            Log.i(COMPILATION_MODEL, "$response")

            LoadResult.Page(
                data = response.map { compilationDto -> compilationDto.toCompilation() },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                // Avoids duplicates
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / NETWORK_PAGE_SIZE)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e(
                COMPILATION_MODEL,
                e.localizedMessage ?: "Network error retrieving Posts",
                e
            )
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            Log.e(
                COMPILATION_MODEL,
                e.localizedMessage ?: "HTTP error retrieving Posts",
                e
            )
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Compilation>): Int {
        return INITIAL_PAGE
    }
}
