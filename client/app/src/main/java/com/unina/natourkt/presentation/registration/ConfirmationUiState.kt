package com.unina.natourkt.presentation.registration

import com.unina.natourkt.common.DataState

/**
 * UiState used for presentation by [RegistrationViewModel]
 */
data class ConfirmationUiState(
    val isLoading: Boolean = false,
    val errorMessage: DataState.Cause? = null,
    val isConfirmationComplete: Boolean = false,
    val isCodeResent: Boolean = false,
)

data class ConfirmationFormUiState(
    val code: String = "",
    val isCodeValid: Boolean = false,
)