package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.RouteStop
import com.unina.natourkt.core.presentation.model.RouteStopUi
import javax.inject.Inject

class RouteStopUiMapper @Inject constructor() : UiMapper<RouteStop, RouteStopUi> {

    override fun mapToUi(domainEntity: RouteStop): RouteStopUi {
        return RouteStopUi(
            stopNumber = domainEntity.stopNumber,
            latitude = domainEntity.latitude,
            longitude = domainEntity.longitude
        )
    }

    fun mapToDomain(uiEntity: RouteStopUi): RouteStop {
        return RouteStop(
            stopNumber = uiEntity.stopNumber,
            latitude = uiEntity.latitude,
            longitude = uiEntity.longitude
        )
    }
}