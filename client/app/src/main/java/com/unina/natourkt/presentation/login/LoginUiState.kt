package com.unina.natourkt.presentation.login

/**
 * UiState used for presentation by [LoginViewModel]
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserLoggedIn: Boolean = false,
)
