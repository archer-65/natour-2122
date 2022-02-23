package com.unina.natourkt.presentation.forgot_password.reset

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [ResetPasswordViewModel]
 */
data class ResetPasswordUiState (
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val isPasswordReset: Boolean = false,
)