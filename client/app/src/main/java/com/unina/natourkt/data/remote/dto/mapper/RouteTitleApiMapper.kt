package com.unina.natourkt.data.remote.dto.mapper

import com.unina.natourkt.data.remote.dto.RouteTitleDto
import com.unina.natourkt.domain.model.RouteTitle
import javax.inject.Inject

class RouteTitleApiMapper @Inject constructor() : ApiMapper<RouteTitleDto, RouteTitle> {

    override fun mapToDomain(apiEntity: RouteTitleDto): RouteTitle {
        return RouteTitle(
            id = apiEntity.id,
            title = apiEntity.title
        )
    }

    fun mapToDto(domainEntity: RouteTitle): RouteTitleDto {
        return RouteTitleDto(
            id = domainEntity.id,
            title = domainEntity.title
        )
    }
}