package com.unina.natourkt.presentation.registration

data class ConfirmationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isConfirmationComplete: Boolean = false,
)
