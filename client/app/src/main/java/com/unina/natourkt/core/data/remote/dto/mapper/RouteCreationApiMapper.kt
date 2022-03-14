package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.RouteCreationDto
import com.unina.natourkt.core.domain.model.RouteCreation
import javax.inject.Inject

class RouteCreationApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper,
    private val routeStopApiMapper: RouteStopApiMapper,
) : CreationApiMapper<RouteCreation, RouteCreationDto> {

    override fun mapToDto(domainEntity: RouteCreation): RouteCreationDto {
        return RouteCreationDto(
            title = domainEntity.title,
            description = domainEntity.description,
            difficulty = domainEntity.avgDifficulty,
            duration = domainEntity.avgDuration,
            isDisabilityFriendly = domainEntity.disabilityFriendly,
            photos = domainEntity.photos.map { RouteCreationDto.Photo(it) },
            stops = domainEntity.stops.map { routeStopApiMapper.mapToDto(it) },
            author = userApiMapper.mapToDto(domainEntity.author!!)
        )
    }
}