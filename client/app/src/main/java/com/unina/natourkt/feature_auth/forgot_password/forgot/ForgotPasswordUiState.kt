package com.unina.natourkt.feature_auth.forgot_password.forgot

import com.unina.natourkt.core.presentation.util.TextFieldState
import com.unina.natourkt.core.util.DataState

/**
 * UiState used for presentation by [ForgotPasswordViewModel]
 */
data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isCodeSent: Boolean = false,
)

data class ForgotPasswordFormUiState(
    val username: TextFieldState = TextFieldState(),
) {
    val isButtonEnabled: Boolean
        get() = username.text.isNotBlank()
}