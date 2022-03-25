package com.unina.natourkt.core.domain.use_case.report

import android.util.Log
import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to delete a report
 * @see [ReportRepository]
 */
class DeleteReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {

    operator fun invoke(reportId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.REPORT_MODEL,
            "Deleting report with ID: ${reportId}"
        )

        val result = reportRepository.deleteReport(reportId)
        emit(result)
    }
}