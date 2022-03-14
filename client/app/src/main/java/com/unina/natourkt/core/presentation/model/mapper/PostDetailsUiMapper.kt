package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.presentation.model.PostDetailsUi
import javax.inject.Inject

class PostDetailsUiMapper @Inject constructor() : UiMapper<Post, PostDetailsUi> {

    override fun mapToUi(domainEntity: Post): PostDetailsUi {
        return PostDetailsUi(
            id = domainEntity.id,
            description = domainEntity.description,
            photos = domainEntity.photos,
            authorId = domainEntity.author.id,
            authorUsername = domainEntity.author.username,
            authorPhoto = domainEntity.author.profilePhoto,
            routeId = domainEntity.taggedRoute.id,
            routeTitle = domainEntity.taggedRoute.title
        )
    }
}