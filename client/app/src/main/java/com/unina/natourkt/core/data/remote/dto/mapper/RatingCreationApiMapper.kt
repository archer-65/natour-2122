package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.RatingCreationDto
import com.unina.natourkt.core.domain.model.RatingCreation
import javax.inject.Inject

class RatingCreationApiMapper @Inject constructor() :
    CreationApiMapper<RatingCreation, RatingCreationDto> {

    override fun mapToDto(domainEntity: RatingCreation): RatingCreationDto {
        return RatingCreationDto(
            ratingDifficulty = domainEntity.difficulty.value,
            ratingDuration = domainEntity.duration,
            authorId = domainEntity.author!!.id,
            ratedRouteId = domainEntity.ratedRouteId
        )
    }
}