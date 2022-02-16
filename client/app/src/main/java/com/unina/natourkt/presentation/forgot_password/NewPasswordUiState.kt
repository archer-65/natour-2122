package com.unina.natourkt.presentation.forgot_password

import com.unina.natourkt.common.DataState

data class NewPasswordUiState (
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val isPasswordReset: Boolean = false,
)