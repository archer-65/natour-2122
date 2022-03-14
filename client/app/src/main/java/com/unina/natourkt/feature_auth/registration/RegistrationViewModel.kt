package com.unina.natourkt.feature_auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.auth.RegistrationConfirmationUseCase
import com.unina.natourkt.core.domain.use_case.auth.RegistrationUseCase
import com.unina.natourkt.core.domain.use_case.auth.ResendConfirmationCodeUseCase
import com.unina.natourkt.core.presentation.base.validation.isCodeValid
import com.unina.natourkt.core.presentation.base.validation.isEmailValid
import com.unina.natourkt.core.presentation.base.validation.isPasswordValid
import com.unina.natourkt.core.presentation.base.validation.isUsernameValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    val uiRegistrationState = _uiRegistrationState.asStateFlow()

    private val _uiRegistrationFormState = MutableStateFlow(RegistrationFormUiState())
    val uiRegistrationFormState = _uiRegistrationFormState.asStateFlow()

    /**
     * [ConfirmationUiState] wrapped by StateFlow, used by [ConfirmationFragment]
     */
    private val _uiConfirmationState = MutableStateFlow(ConfirmationUiState())
    val uiConfirmationState = _uiConfirmationState.asStateFlow()

    private val _uiConfirmationFormState = MutableStateFlow(ConfirmationFormUiState())
    val uiConfirmationFormState = _uiConfirmationFormState.asStateFlow()

    /**
     * Registration function
     * @see [RegistrationUseCase]
     */
    fun registration() = with(uiRegistrationFormState.value) {
        // On every value emitted by the flow
        registrationUseCase(username, email, password).onEach { result ->
            when (result) {
                // In case of success, update the isSignUpComplete valueu
                is DataState.Success -> {
                    _uiRegistrationState.value =
                        RegistrationUiState(isSignUpComplete = result.data ?: false)
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


    /**
     * Confirmation function
     * @see [RegistrationConfirmationUseCase]
     */
    fun confirmation() {
        // On every value emitted by the flow
        registrationConfirmationUseCase(
            _uiRegistrationFormState.value.username,
            _uiConfirmationFormState.value.code
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

    /**
     * Resend code function
     * @see [ResendConfirmationCodeUseCase]
     */
    fun resendCode() {
        // On every value emitted by the flow
        resendConfirmationCodeUseCase(_uiRegistrationFormState.value.username).onEach { result ->
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

    fun setUsername(username: String) {
        _uiRegistrationFormState.update {
            it.copy(username = username, isUsernameValid = username.isUsernameValid())
        }
    }

    fun setEmail(email: String) {
        _uiRegistrationFormState.update {
            it.copy(email = email, isEmailValid = email.isEmailValid())
        }
    }

    fun setPassword(password: String) {
        _uiRegistrationFormState.update {
            it.copy(password = password, isPasswordValid = password.isPasswordValid())
        }
    }

    fun setConfirmPassword(confirmPassword: String) {
        _uiRegistrationFormState.update {
            it.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = confirmPassword.equals(it.password)
            )
        }
    }

    fun setCode(code: String) {
        _uiConfirmationFormState.update {
            it.copy(code = code, isCodeValid = code.isCodeValid())
        }
    }
}