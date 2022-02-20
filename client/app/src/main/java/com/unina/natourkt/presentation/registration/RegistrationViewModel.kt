package com.unina.natourkt.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.RegistrationConfirmationUseCase
import com.unina.natourkt.domain.use_case.auth.RegistrationUseCase
import com.unina.natourkt.domain.use_case.auth.ResendConfirmationCodeUseCase
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
    private val registrationConfirmationUseCase: RegistrationConfirmationUseCase,
    private val resendConfirmationCodeUseCase: ResendConfirmationCodeUseCase,
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
            // On every value emitted by the flow
            registrationUseCase(username, email, password).onEach { result ->
                when (result) {
                    // In case of success, update the isSignUpComplete value and
                    // set the username (will be used by confirmation)
                    is DataState.Success -> {
                        _uiRegistrationState.value =
                            RegistrationUiState(
                                isSignUpComplete = result.data ?: false,
                                username = username
                            )
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiRegistrationState.value =
                            RegistrationUiState(errorMessage = result.error)
                    }
                    // In case of loading state, isLoading is true
                    is DataState.Loading -> {
                        _uiRegistrationState.value = RegistrationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Confirmation function
     * @see [RegistrationConfirmationUseCase]
     */
    fun confirmation(code: String) {

        viewModelScope.launch {
            // On every value emitted by the flow
            registrationConfirmationUseCase(
                uiRegistrationState.value.username!!,
                code
            ).onEach { result ->
                when (result) {
                    // In case of success, update the isConfirmationComplete value
                    is DataState.Success -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isConfirmationComplete = result.data ?: false)
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        // In case of loading state, isLoading is true
                        _uiConfirmationState.value =
                            ConfirmationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Resend code funciont
     * @see [ResendConfirmationCodeUseCase]
     */
    fun resendCode() {

        viewModelScope.launch {
            // On every value emitted by the flow
            resendConfirmationCodeUseCase(uiRegistrationState.value.username!!).onEach { result ->
                when (result) {
                    // In case of success, update the isCoderResent value
                    is DataState.Success -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isCodeResent = result.data ?: false)
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(errorMessage = result.error)
                    }
                    // In case of loading state, isLoading is true
                    is DataState.Loading -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}