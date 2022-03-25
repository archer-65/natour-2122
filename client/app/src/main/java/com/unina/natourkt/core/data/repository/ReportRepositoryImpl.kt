package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.data.paging.ReportsSource
import com.unina.natourkt.core.data.remote.dto.mapper.DirectionsApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.ReportApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.ReportCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.MapsApi
import com.unina.natourkt.core.data.remote.retrofit.ReportApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.domain.repository.MapsRepository
import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [ReportRepository] works with a [ReportApi] Retrofit interface
 * It also contains mapper for model conversions
 * @see [ReportCreationApiMapper]
 * @see [ReportApiMapper]
 */
class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApi,
    private val reportCreationApiMapper: ReportCreationApiMapper,
    private val reportApiMapper: ReportApiMapper,
) : ReportRepository {

    /**
     * Page size for network requests for this class
     * NOTE: The first page defaults to pageSize * 3!
     */
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    override suspend fun createReport(report: ReportCreation): DataState<Unit> =
        safeApiCall(IO) {
            val reportRequest = reportCreationApiMapper.mapToDto(report)
            api.createReport(reportRequest)
        }

    override fun getReports(): Flow<PagingData<Report>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ReportsSource(api, reportApiMapper) }
        ).flow
    }

    override suspend fun deleteReport(reportId: Long): DataState<Unit> =
        safeApiCall(IO) {
            api.deleteReport(reportId)
        }
}