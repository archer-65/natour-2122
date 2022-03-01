package com.unina.natourkt.presentation.login

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [LoginViewModel]
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessage? = null,
    val isUserLoggedIn: Boolean = false,
)

data class LoginFormUiState(
    val username: String = "",
    val password: String = "",
    val isUsernameValid: Boolean = false,
    val isPasswordValid: Boolean = false,
)