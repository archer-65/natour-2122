package com.unina.natourkt.feature_auth.forgot_password.reset

import com.unina.natourkt.core.presentation.util.TextFieldState
import com.unina.natourkt.core.util.DataState

/**
 * UiState used for presentation by [ResetPasswordViewModel]
 */
data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isPasswordReset: Boolean = false,
)

data class ResetPasswordFormUiState(
    val code: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val confirmPassword: TextFieldState = TextFieldState()
) {
    val isButtonEnabled: Boolean
        get() = code.text.isNotBlank() &&
                password.text.isNotBlank() && confirmPassword.text.isNotBlank()
}