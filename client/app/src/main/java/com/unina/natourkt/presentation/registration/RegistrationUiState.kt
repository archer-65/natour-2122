package com.unina.natourkt.presentation.registration

data class RegistrationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpComplete: Boolean = false,
    val isConfirmationComplete: Boolean = false,
    val username: String? = null
)
