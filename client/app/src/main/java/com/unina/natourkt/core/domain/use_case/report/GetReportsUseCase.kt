package com.unina.natourkt.core.domain.use_case.report

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve all reports
 * @see [ReportRepository]
 */
class GetReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {

    operator fun invoke(): Flow<PagingData<Report>> {
        Log.i(Constants.REPORT_MODEL, "Getting reports...")
        return reportRepository.getReports()
    }
}