package com.unina.natourkt.presentation.registration

import com.unina.natourkt.common.DataState
import com.unina.natourkt.presentation.login.LoginViewModel

/**
 * UiState used for presentation by [RegistrationViewModel]
 */
data class ConfirmationUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val isConfirmationComplete: Boolean = false,
    val isCodeResent: Boolean = false,
)
