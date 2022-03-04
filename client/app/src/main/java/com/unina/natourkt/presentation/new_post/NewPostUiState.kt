package com.unina.natourkt.presentation.new_post

import android.net.Uri
import com.unina.natourkt.common.DataState

data class NewPostUiState(
    val isInserted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessage? = null,
    val postDescription: String = "",
    val routeId: Long? = null,
    val postPhotos: List<Uri> = emptyList(),
)

data class RouteResultsUiState(
    val routes: List<UpcomingRoute> = emptyList(),
)

data class UpcomingRoute(
    val routeTitle: String,
    val routeId: Long,
)