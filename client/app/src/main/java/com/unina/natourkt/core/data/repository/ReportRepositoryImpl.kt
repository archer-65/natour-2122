package com.unina.natourkt.core.data.repository

import com.unina.natourkt.core.data.remote.dto.mapper.ReportCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.ReportApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApi,
    private val reportCreationApiMapper: ReportCreationApiMapper,
) : ReportRepository {

    override suspend fun createReport(report: ReportCreation): DataState<Unit> =
        safeApiCall(Dispatchers.IO) {
            val reportRequest = reportCreationApiMapper.mapToDto(report)
            api.createReport(reportRequest)
        }
}