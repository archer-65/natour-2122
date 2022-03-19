package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.ReportDto
import com.unina.natourkt.core.domain.model.Report
import javax.inject.Inject

class ReportApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper,
    private val routeTitleApiMapper: RouteTitleApiMapper,
) : ApiMapper<ReportDto, Report> {

    override fun mapToDomain(apiEntity: ReportDto): Report {
        return Report(
            id = apiEntity.reportId,
            title = apiEntity.reportTitle,
            description = apiEntity.reportDescription,
            author = userApiMapper.mapToDomain(apiEntity.author),
            reportedRoute = routeTitleApiMapper.mapToDomain(apiEntity.reportedRoute)
        )
    }
}