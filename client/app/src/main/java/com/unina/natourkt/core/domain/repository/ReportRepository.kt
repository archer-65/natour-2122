package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for report functions repository
 */
interface ReportRepository {

    /**
     * This function creates a compilation taking only a [ReportCreation] model as
     * parameter
     */
    suspend fun createReport(report: ReportCreation): DataState<Unit>

    /**
     * This function gets all reports
     */
    fun getReports(): Flow<PagingData<Report>>

    /**
     * This functions delete a report given its id
     */
    suspend fun deleteReport(reportId: Long): DataState<Unit>
}