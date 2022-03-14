package com.unina.natourkt.feature_auth.login

import com.unina.natourkt.core.util.DataState

/**
 * UiState used for presentation by [LoginViewModel]
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val isUserLoggedIn: Boolean = false,
)

data class LoginFormUiState(
    val username: String = "",
    val password: String = "",
    val isUsernameValid: Boolean = false,
    val isPasswordValid: Boolean = false,
)