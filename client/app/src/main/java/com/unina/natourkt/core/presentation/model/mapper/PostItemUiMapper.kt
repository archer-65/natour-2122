package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.presentation.model.PostItemUi
import javax.inject.Inject

class PostItemUiMapper @Inject constructor() : UiMapper<Post, PostItemUi> {

    override fun mapToUi(domainEntity: Post): PostItemUi {
        return PostItemUi(
            id = domainEntity.id,
            description = domainEntity.description,
            photos = domainEntity.photos,
            authorId = domainEntity.author.id,
            authorUsername = domainEntity.author.username,
            authorPhoto = domainEntity.author.profilePhoto ?: "",
            routeId = domainEntity.taggedRoute.id,
            routeTitle = domainEntity.taggedRoute.title
        )
    }
}