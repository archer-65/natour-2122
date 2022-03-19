package com.unina.natourkt.core.domain.use_case.report

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {

    /**
     * Get [Flow] of [PagingData] for [Report] model
     */
    operator fun invoke(): Flow<PagingData<Report>> {
        Log.i(Constants.ROUTE_MODEL, "Getting paginated reports...")
        return reportRepository.getReports()
    }
}