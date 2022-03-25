package com.unina.natourkt.core.domain.use_case.report

import android.util.Log
import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to report a route
 * @see [ReportRepository]
 */
class CreateRouteReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {

    operator fun invoke(report: ReportCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.REPORT_MODEL,
            "Reporting route with ID: ${report.reportedRouteId} authored by user with ID: ${report.author?.id}"
        )

        val result = reportRepository.createReport(report)
        emit(result)
    }
}