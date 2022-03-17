package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.ReportCreationDto
import com.unina.natourkt.core.domain.model.ReportCreation
import javax.inject.Inject

class ReportCreationApiMapper @Inject constructor() :
    CreationApiMapper<ReportCreation, ReportCreationDto> {

    override fun mapToDto(domainEntity: ReportCreation): ReportCreationDto {
        return ReportCreationDto(
            reportTitle = domainEntity.title,
            reportDescription = domainEntity.description,
            authorId = domainEntity.author?.id ?: -1,
            reportedRouteId = domainEntity.reportedRouteId
        )
    }
}