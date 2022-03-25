package com.unina.natourkt.feature_auth.registration.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.auth.RegistrationUseCase
import com.unina.natourkt.core.presentation.base.validation.equalsOtherString
import com.unina.natourkt.core.presentation.base.validation.isEmailValid
import com.unina.natourkt.core.presentation.base.validation.isPasswordValid
import com.unina.natourkt.core.presentation.base.validation.isUsernameValid
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * SharedViewModel used by [RegistrationFragment] and [ConfirmationFragment]
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase
) : ViewModel() {

    /**
     * [RegistrationUiState] wrapped by StateFlow used by [RegistrationFragment]
     */
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(RegistrationFormUiState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.EnteredUsername -> setUsername(event.username)
            is RegistrationEvent.EnteredEmail -> setEmail(event.email)
            is RegistrationEvent.EnteredPassword -> setPassword(event.password)
            is RegistrationEvent.EnteredConfirmPassword -> setConfirmPassword(event.confirmPassword)
            RegistrationEvent.Registration -> registration()
        }
    }

    /**
     * Registration function
     * @see [RegistrationUseCase]
     */
    private fun registration() = with(formState.value) {
        // On every value emitted by the flow
        if (checkFormValidity()) {
            registrationUseCase(username.text, email.text, password.text).onEach { result ->
                when (result) {
                    // In case of success, update the isSignUpComplete value
                    is DataState.Success -> {
                        _uiState.value =
                            RegistrationUiState(isSignUpComplete = result.data ?: false)

                        analyticsUseCase.sendEvent(ActionEvents.SignUpSubmit)
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiState.value = RegistrationUiState(isLoading = false)

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    // In case of loading state, isLoading is true
                    is DataState.Loading -> {
                        _uiState.value = RegistrationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setUsername(username: String) {
        _formState.update {
            it.copy(username = it.username.copy(text = username))
        }
    }

    private fun setEmail(email: String) {
        _formState.update {
            it.copy(email = it.email.copy(text = email))
        }
    }

    private fun setPassword(password: String) {
        _formState.update {
            it.copy(password = it.password.copy(text = password))
        }
    }

    private fun setConfirmPassword(confirmPassword: String) {
        _formState.update {
            it.copy(confirmPassword = it.confirmPassword.copy(text = confirmPassword))
        }
    }

    private fun checkFormValidity(): Boolean {
        val usernameError = formState.value.username.text.isUsernameValid()

        val emailError = formState.value.email.text.isEmailValid()

        val passwordError = formState.value.password.text.isPasswordValid()

        val confirmPasswordError =
            formState.value.confirmPassword.text.equalsOtherString(_formState.value.password.text)

        _formState.update {
            it.copy(
                username = it.username.copy(error = usernameError),
                email = it.email.copy(error = emailError),
                password = it.password.copy(error = passwordError),
                confirmPassword = it.password.copy(error = confirmPasswordError)
            )
        }

        return usernameError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null
    }
}