package com.unina.natourkt.feature_auth.login

import com.unina.natourkt.core.presentation.util.TextFieldState

/**
 * UiState used for presentation by [LoginViewModel]
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
)

data class LoginFormUiState(
    val username: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
) {
    val isButtonEnabled: Boolean
        get() = username.text.isNotBlank() && password.text.isNotBlank()
}