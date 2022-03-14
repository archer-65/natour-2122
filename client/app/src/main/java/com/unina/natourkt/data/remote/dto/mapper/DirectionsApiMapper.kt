package com.unina.natourkt.data.remote.dto.mapper

import com.unina.natourkt.data.remote.dto.DirectionsDto
import com.unina.natourkt.data.util.DirectionParser
import com.unina.natourkt.domain.model.DirectionsPolyline
import javax.inject.Inject

class DirectionsApiMapper @Inject constructor() : ApiMapper<DirectionsDto, DirectionsPolyline> {

    override fun mapToDomain(apiEntity: DirectionsDto): DirectionsPolyline {
        return DirectionsPolyline(
            points = apiEntity.routes.flatMap {
                DirectionParser.decode(it.overviewPolyline.points)
            }
        )
    }
}