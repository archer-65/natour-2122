package com.unina.natourkt.feature_auth.forgot_password.forgot

import com.unina.natourkt.core.util.DataState

/**
 * UiState used for presentation by [ForgotPasswordViewModel]
 */
data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val isCodeSent: Boolean = false,
)

data class ForgotPasswordFormUiState(
    val username: String = "",
    val isUsernameValid: Boolean = false,
)