package com.unina.natourkt.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unina.natourkt.core.data.remote.dto.mapper.ReportApiMapper
import com.unina.natourkt.core.data.remote.retrofit.ReportApi
import com.unina.natourkt.core.data.repository.ReportRepositoryImpl
import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.util.Constants
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 0

class ReportsSource @Inject constructor(
    private val api: ReportApi,
    private val reportApiMapper: ReportApiMapper,
) : PagingSource<Int, Report>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Report> {

        return try {
            val position = params.key ?: INITIAL_PAGE

            val response = api.getReports(position, params.loadSize)
            Log.i(Constants.REPORT_MODEL, "$response")

            LoadResult.Page(
                data = response.map { dto -> reportApiMapper.mapToDomain(dto) },
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                // Avoids duplicates
                nextKey = if (response.isEmpty()) null else position + (params.loadSize / ReportRepositoryImpl.NETWORK_PAGE_SIZE)
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e(Constants.REPORT_MODEL, e.localizedMessage ?: "Network error retrieving Routes", e)
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            Log.e(Constants.REPORT_MODEL, e.localizedMessage ?: "HTTP error retrieving Routes", e)
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Report>): Int {
        return INITIAL_PAGE
    }
}