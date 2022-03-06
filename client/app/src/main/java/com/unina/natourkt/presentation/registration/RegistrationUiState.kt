package com.unina.natourkt.presentation.registration

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [RegistrationViewModel]
 */
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val isSignUpComplete: Boolean = false,
    val isConfirmationComplete: Boolean = false,
)

data class RegistrationFormUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isUsernameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
)