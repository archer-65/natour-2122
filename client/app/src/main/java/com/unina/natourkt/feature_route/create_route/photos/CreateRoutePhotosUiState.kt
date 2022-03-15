package com.unina.natourkt.feature_route.create_route.photos

import android.net.Uri

data class CreateRoutePhotosUiState(
    val photos: List<Uri> = emptyList()
) {

    val isButtonEnabled: Boolean
        get() = photos.isNotEmpty()
}