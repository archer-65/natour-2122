package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.presentation.model.PostGridItemUi
import javax.inject.Inject

class PostGridItemUiMapper @Inject constructor() : UiMapper<Post, PostGridItemUi> {

    override fun mapToUi(domainEntity: Post): PostGridItemUi {
        return PostGridItemUi(
            id = domainEntity.id,
            previewPhoto = domainEntity.photos.firstOrNull() ?: "",
            authorId = domainEntity.author.id
        )
    }
}