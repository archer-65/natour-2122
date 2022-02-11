package com.unina.natourkt.presentation.login

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [LoginViewModel]
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val isUserLoggedIn: Boolean = false,
)
