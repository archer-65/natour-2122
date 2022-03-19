package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.Report
import com.unina.natourkt.core.presentation.model.ReportItemUi
import javax.inject.Inject

class ReportItemUiMapper @Inject constructor(
    private val routeTitleUiMapper: RouteTitleUiMapper,
) : UiMapper<Report, ReportItemUi> {

    override fun mapToUi(domainEntity: Report): ReportItemUi {
        return ReportItemUi(
            id = domainEntity.id,
            title = domainEntity.title,
            description = domainEntity.description,
            authorPhoto = domainEntity.author?.profilePhoto ?: "",
            reportedRoute = routeTitleUiMapper.mapToUi(domainEntity.reportedRoute)
        )
    }
}