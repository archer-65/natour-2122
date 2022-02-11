package com.unina.natourkt.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.ConfirmationUseCase
import com.unina.natourkt.domain.use_case.auth.RegistrationUseCase
import com.unina.natourkt.domain.use_case.auth.ResendCodeUseCase
import com.unina.natourkt.presentation.registration.confirmation.ConfirmationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SharedViewModel used by [RegistrationFragment] and [ConfirmationFragment]
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val confirmationUseCase: ConfirmationUseCase,
    private val resendCodeUseCase: ResendCodeUseCase,
) : ViewModel() {

    /**
     * [RegistrationUiState] wrapped by StateFlow used by [RegistrationFragment]
     */
    private val _uiRegistrationState = MutableStateFlow(RegistrationUiState())
    val uiRegistrationState: StateFlow<RegistrationUiState> = _uiRegistrationState.asStateFlow()

    /**
     * [ConfirmationUiState] wrapped by StateFlow, used by [ConfirmationFragment]
     */
    private val _uiConfirmationState = MutableStateFlow(ConfirmationUiState())
    val uiConfirmationState: StateFlow<ConfirmationUiState> = _uiConfirmationState.asStateFlow()

    /**
     * Registration function
     * @see [RegistrationUseCase]
     */
    fun registration(username: String, email: String, password: String) {

        viewModelScope.launch {
            registrationUseCase(username, email, password).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiRegistrationState.value =
                            RegistrationUiState(
                                isSignUpComplete = result.data ?: false,
                                username = username
                            )
                    }
                    is DataState.Error -> {
                        _uiRegistrationState.value =
                            RegistrationUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiRegistrationState.value = RegistrationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Confirmation function
     * @see [ConfirmationUseCase]
     */
    fun confirmation(code: String) {

        viewModelScope.launch {
            confirmationUseCase(uiRegistrationState.value.username!!, code).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isConfirmationComplete = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Resend code funciont
     * @see [ResendCodeUseCase]
     */
    fun resendCode() {

        viewModelScope.launch {
            resendCodeUseCase(uiRegistrationState.value.username!!).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isCodeResent = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}