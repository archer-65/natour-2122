package com.unina.natourkt.presentation.forgot_password.reset

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [ResetPasswordViewModel]
 */
data class ResetPasswordUiState (
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessage? = null,
    val isPasswordReset: Boolean = false,
)

data class ResetPasswordFormUiState(
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isCodeValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
)