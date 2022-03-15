package com.unina.natourkt.feature_post.create_post

import android.net.Uri
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.presentation.model.RouteTitleUi

data class NewPostUiState(
    val isInserted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val postDescription: String = "",
    val route: RouteTitleUi? = null,
    val postPhotos: List<Uri> = emptyList(),
)

data class RouteResultsUiState(
    val routes: List<RouteTitleUi> = emptyList(),
)

fun RouteTitleUi.toRouteTitle(): RouteTitle {
    return RouteTitle(
        id = routeId,
        title = routeTitle,
    )
}

fun NewPostUiState.toPostCreation(): PostCreation {
    return PostCreation(
        description = postDescription,
        photos = postPhotos.map { it.toString() },
        author = null,
        taggedRoute = route!!.toRouteTitle(),
    )
}