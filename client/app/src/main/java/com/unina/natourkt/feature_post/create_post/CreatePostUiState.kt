package com.unina.natourkt.feature_post.create_post

import android.net.Uri
import com.unina.natourkt.core.presentation.model.RouteTitleUi
import com.unina.natourkt.core.presentation.util.TextFieldState

data class CreatePostUiState(
    val isLoading: Boolean = false,
    val isInserted: Boolean = false,
    val postDescription: TextFieldState = TextFieldState(),
    val route: RouteTitleUi? = null,
    val postPhotos: List<Uri> = emptyList(),
) {

    val isButtonEnabled: Boolean
        get() = route != null && postPhotos.size >= 1
}