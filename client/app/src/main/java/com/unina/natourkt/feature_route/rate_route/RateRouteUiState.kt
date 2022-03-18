package com.unina.natourkt.feature_route.rate_route

import com.unina.natourkt.core.presentation.util.TextFieldState
import com.unina.natourkt.core.util.Difficulty

data class RateRouteUiState(
    val isLoading: Boolean = false,
    val isInserted: Boolean = false,
    val duration: TextFieldState = TextFieldState(text = "1"),
    val difficulty: Difficulty = Difficulty.EASY
) {

    val isButtonEnabled: Boolean
        get() = duration.text.isNotBlank()
}