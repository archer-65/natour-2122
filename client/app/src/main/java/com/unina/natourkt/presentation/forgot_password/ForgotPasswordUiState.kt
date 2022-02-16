package com.unina.natourkt.presentation.forgot_password

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [ForgotPasswordViewModel]
 */
data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val isCodeSent: Boolean = false,
)