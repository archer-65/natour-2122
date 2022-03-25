package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.route.RouteDto
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.util.DateTimeParser
import com.unina.natourkt.core.util.Difficulty
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
            creationDate = DateTimeParser.parseDateTime(apiEntity.creationDate),
            modifiedDate = if (apiEntity.modifiedDate.isNullOrEmpty()) null else DateTimeParser.parseDateTime(
                apiEntity.modifiedDate
            ),
            isReported = apiEntity.isRouteReported,
            photos = apiEntity.photos?.map { it.photo } ?: emptyList(),
            stops = apiEntity.stops.map { routeStopApiMapper.mapToDomain(it) },
            author = userApiMapper.mapToDomain(apiEntity.author)
        )
    }

    fun mapToDto(domainEntity: Route): RouteDto {
        return RouteDto(
            id = domainEntity.id,
            title = domainEntity.title,
            description = domainEntity.description,
            duration = domainEntity.duration,
            difficulty = domainEntity.difficulty.value,
            isDisabilityFriendly = domainEntity.disabilityFriendly,
            creationDate = domainEntity.creationDate.toString(),
            modifiedDate = null,
            isRouteReported = domainEntity.isReported,
            photos = null,
            stops = domainEntity.stops.map { routeStopApiMapper.mapToDto(it) },
            author = userApiMapper.mapToDto(domainEntity.author)
        )
    }
}