package com.unina.natourkt.feature_route.create_route.info

import com.unina.natourkt.core.presentation.util.TextFieldState
import com.unina.natourkt.core.util.Difficulty

data class CreateRouteInfoUiState(
    val routeTitle: TextFieldState = TextFieldState(),
    val routeDescription: TextFieldState = TextFieldState(),
    val duration: TextFieldState = TextFieldState(text = "1"),
    val disabilityFriendly: Boolean = false,
    val difficulty: Difficulty = Difficulty.EASY,
) {

    val isButtonEnabled: Boolean
        get() = routeTitle.text.isNotBlank() && duration.text.isNotBlank()
}