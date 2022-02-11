package com.unina.natourkt.presentation.registration

import com.unina.natourkt.common.DataState
import com.unina.natourkt.presentation.login.LoginViewModel

/**
 * UiState used for presentation by [RegistrationViewModel]
 */
data class RegistrationUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.CustomMessages? = null,
    val isSignUpComplete: Boolean = false,
    val isConfirmationComplete: Boolean = false,
    val username: String? = null,
)
