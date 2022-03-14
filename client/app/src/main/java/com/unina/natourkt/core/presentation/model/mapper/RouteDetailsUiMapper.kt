package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.presentation.model.RouteDetailsUi
import javax.inject.Inject

class RouteDetailsUiMapper @Inject constructor(
    private val routeStopUiMapper: RouteStopUiMapper
) : UiMapper<Route, RouteDetailsUi> {

    override fun mapToUi(domainEntity: Route): RouteDetailsUi {
        return RouteDetailsUi(
            id = domainEntity.id,
            title = domainEntity.title,
            description = domainEntity.description,
            difficulty = domainEntity.difficulty,
            duration = domainEntity.duration,
            disabilityFriendly = domainEntity.disabilityFriendly,
            modifiedDate = domainEntity.modifiedDate,
            isReported = domainEntity.isReported,
            photos = domainEntity.photos,
            stops = domainEntity.stops.map { routeStopUiMapper.mapToUi(it) },
            authorId = domainEntity.author.id,
        )
    }
}