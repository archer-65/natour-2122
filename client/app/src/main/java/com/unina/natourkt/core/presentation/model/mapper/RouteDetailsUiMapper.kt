package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.presentation.model.RouteDetailsUi
import javax.inject.Inject

class RouteDetailsUiMapper @Inject constructor(
    private val routeStopUiMapper: RouteStopUiMapper,
    private val userUiMapper: UserUiMapper,
) : UiMapper<Route, RouteDetailsUi> {

    override fun mapToUi(domainEntity: Route): RouteDetailsUi {
        return RouteDetailsUi(
            id = domainEntity.id,
            title = domainEntity.title,
            description = domainEntity.description,
            difficulty = domainEntity.difficulty,
            duration = domainEntity.duration,
            disabilityFriendly = domainEntity.disabilityFriendly,
            creationDate = domainEntity.creationDate,
            modifiedDate = domainEntity.modifiedDate,
            isReported = domainEntity.isReported,
            photos = domainEntity.photos,
            stops = domainEntity.stops.map { routeStopUiMapper.mapToUi(it) },
            author = userUiMapper.mapToUi(domainEntity.author),
        )
    }

    fun mapToDomain(uiEntity: RouteDetailsUi): Route {
        return Route(
            id = uiEntity.id,
            title = uiEntity.title,
            description = uiEntity.description,
            difficulty = uiEntity.difficulty,
            duration = uiEntity.duration,
            disabilityFriendly = uiEntity.disabilityFriendly,
            creationDate = uiEntity.creationDate,
            modifiedDate = uiEntity.modifiedDate,
            isReported = uiEntity.isReported,
            photos = uiEntity.photos,
            stops = uiEntity.stops.map { routeStopUiMapper.mapToDomain(it) },
            author = userUiMapper.mapToDomain(uiEntity.author)
        )
    }
}