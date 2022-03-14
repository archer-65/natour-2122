package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.presentation.model.RouteTitleUi
import javax.inject.Inject

class RouteTitleUiMapper @Inject constructor() : UiMapper<RouteTitle, RouteTitleUi> {

    override fun mapToUi(domainEntity: RouteTitle): RouteTitleUi {
        return RouteTitleUi(
            routeId = domainEntity.id,
            routeTitle = domainEntity.title
        )
    }

    fun mapToDomain(uiEntity: RouteTitleUi): RouteTitle {
        return RouteTitle(
            id = uiEntity.routeId,
            title = uiEntity.routeTitle
        )
    }
}