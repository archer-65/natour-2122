package com.unina.natourkt.feature_auth.registration.signup

import com.unina.natourkt.core.presentation.util.TextFieldState

/**
 * UiState used for presentation by [RegistrationViewModel]
 */
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val isSignUpComplete: Boolean = false,
)

data class RegistrationFormUiState(
    val username: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val confirmPassword: TextFieldState = TextFieldState()
) {
    val isButtonEnabled: Boolean
        get() = username.text.isNotBlank() && email.text.isNotBlank() &&
                password.text.isNotBlank() && confirmPassword.text.isNotBlank()
}