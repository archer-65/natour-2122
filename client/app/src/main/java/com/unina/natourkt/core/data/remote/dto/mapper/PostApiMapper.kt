package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.post.PostDto
import com.unina.natourkt.core.domain.model.Post
import javax.inject.Inject

class PostApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper,
    private val routeTitleApiMapper: RouteTitleApiMapper,
) : ApiMapper<PostDto, Post> {

    override fun mapToDomain(apiEntity: PostDto): Post {
        return Post(
            id = apiEntity.id,
            description = apiEntity.description.orEmpty(),
            isReported = apiEntity.isReported,
            photos = apiEntity.photos.map { it.photo },
            author = userApiMapper.mapToDomain(apiEntity.author),
            taggedRoute = routeTitleApiMapper.mapToDomain(apiEntity.taggedRoute)
        )
    }
}