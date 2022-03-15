package com.unina.natourkt.feature_auth.registration.confirmation

import com.unina.natourkt.core.presentation.util.TextFieldState

/**
 * UiState used for presentation by [RegistrationViewModel]
 */
data class ConfirmationUiState(
    val isLoading: Boolean = false,
    val isConfirmationComplete: Boolean = false,
)

data class ConfirmationFormUiState(
    val code: TextFieldState = TextFieldState()
) {
    val isButtonEnabled: Boolean
        get() = code.text.isNotBlank()
}