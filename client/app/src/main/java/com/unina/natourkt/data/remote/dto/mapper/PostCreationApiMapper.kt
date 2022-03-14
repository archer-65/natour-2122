package com.unina.natourkt.data.remote.dto.mapper

import com.unina.natourkt.data.remote.dto.PostCreationDto
import com.unina.natourkt.domain.model.PostCreation
import javax.inject.Inject

class PostCreationApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper,
    private val routeTitleApiMapper: RouteTitleApiMapper,
) : CreationApiMapper<PostCreation, PostCreationDto> {

    override fun mapToDto(domainEntity: PostCreation): PostCreationDto {
        return PostCreationDto(
            description = domainEntity.description,
            photos = domainEntity.photos.map { PostCreationDto.Photo(it) },
            author = userApiMapper.mapToDto(domainEntity.author!!),
            taggedRoute = routeTitleApiMapper.mapToDto(domainEntity.taggedRoute)
        )
    }
}