package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Filter
import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    suspend fun createReport(report: ReportCreation): DataState<Unit>

    fun getReports(): Flow<PagingData<Report>>
}