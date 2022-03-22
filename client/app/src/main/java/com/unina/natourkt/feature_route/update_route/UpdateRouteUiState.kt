package com.unina.natourkt.feature_route.update_route

import com.unina.natourkt.core.presentation.util.TextFieldState

data class UpdateRouteUiState(
    val isLoading: Boolean = false,
    val isUpdated: Boolean = false,
    val newDescription: TextFieldState = TextFieldState(),
) {

    val isButtonEnabled: Boolean
        get() = newDescription.text.isNotBlank()
}