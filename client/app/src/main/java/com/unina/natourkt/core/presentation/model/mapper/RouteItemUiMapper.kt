package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.presentation.model.RouteItemUi
import javax.inject.Inject

class RouteItemUiMapper @Inject constructor() : UiMapper<Route, RouteItemUi> {

    override fun mapToUi(domainEntity: Route): RouteItemUi {
        return RouteItemUi(
            id = domainEntity.id,
            title = domainEntity.title,
            avgDifficulty = domainEntity.difficulty,
            disabilityFriendly = domainEntity.disabilityFriendly,
            previewPhoto = domainEntity.photos.firstOrNull() ?: "",
            authorId = domainEntity.author.id
        )
    }
}