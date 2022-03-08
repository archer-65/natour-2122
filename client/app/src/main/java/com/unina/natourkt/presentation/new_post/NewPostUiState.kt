package com.unina.natourkt.presentation.new_post

import android.net.Uri
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.PostCreation
import com.unina.natourkt.domain.model.RouteTitle

data class NewPostUiState(
    val isInserted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val postDescription: String = "",
    val route: RouteTitleItemUiState? = null,
    val postPhotos: List<Uri> = emptyList(),
)

data class RouteResultsUiState(
    val routes: List<RouteTitleItemUiState> = emptyList(),
)

data class RouteTitleItemUiState(
    val routeId: Long,
    val routeTitle: String,
)

fun RouteTitleItemUiState.toRouteTitle(): RouteTitle {
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