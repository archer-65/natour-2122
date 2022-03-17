package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.util.DataState

interface ReportRepository {

    suspend fun createReport(report: ReportCreation): DataState<Unit>
}