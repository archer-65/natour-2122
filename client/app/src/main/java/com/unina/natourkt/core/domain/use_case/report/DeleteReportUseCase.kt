package com.unina.natourkt.core.domain.use_case.report

import com.unina.natourkt.core.domain.repository.ReportRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {

    operator fun invoke(reportId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val result = reportRepository.deleteReport(reportId)
        emit(result)
    }
}