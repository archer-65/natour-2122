package com.unina.natourkt.data.remote.dto.mapper

import com.unina.natourkt.data.remote.dto.RouteStopDto
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.model.route.RouteStop
import javax.inject.Inject

class RouteStopApiMapper @Inject constructor() : ApiMapper<RouteStopDto, RouteStop> {

    override fun mapToDomain(apiEntity: RouteStopDto): RouteStop {
        return RouteStop(
            stopNumber = apiEntity.stopNumber,
            latitude = apiEntity.latitude,
            longitude = apiEntity.longitude
        )
    }

    fun mapToDto(domainEntity: RouteStop): RouteStopDto {
        return RouteStopDto(
            stopNumber = domainEntity.stopNumber,
            latitude = domainEntity.latitude,
            longitude = domainEntity.longitude
        )
    }
}