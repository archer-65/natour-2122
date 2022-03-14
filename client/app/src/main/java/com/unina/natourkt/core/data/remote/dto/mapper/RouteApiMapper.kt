package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.util.DateTimeParser
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.core.data.remote.dto.route.RouteDto
import com.unina.natourkt.core.domain.model.route.Route
import javax.inject.Inject

class RouteApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper,
    private val routeStopApiMapper: RouteStopApiMapper,
) : ApiMapper<RouteDto, Route> {

    override fun mapToDomain(apiEntity: RouteDto): Route {
        return Route(
            id = apiEntity.id,
            title = apiEntity.title,
            description = apiEntity.description.orEmpty(),
            duration = apiEntity.duration,
            difficulty = Difficulty.fromInt(apiEntity.difficulty),
            disabilityFriendly = apiEntity.isDisabilityFriendly,
            creationDate = DateTimeParser.parse(apiEntity.creationDate),
            modifiedDate = if (apiEntity.modifiedDate.isNullOrEmpty()) null else DateTimeParser.parse(apiEntity.modifiedDate),
            isReported = apiEntity.isRouteReported,
            photos = apiEntity.photos.map { it.photo },
            stops = apiEntity.stops.map { routeStopApiMapper.mapToDomain(it) },
            author = userApiMapper.mapToDomain(apiEntity.author)
        )
    }
}