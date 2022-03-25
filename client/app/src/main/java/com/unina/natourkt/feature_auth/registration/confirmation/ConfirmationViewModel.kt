package com.unina.natourkt.feature_auth.registration.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.auth.RegistrationConfirmationUseCase
import com.unina.natourkt.core.domain.use_case.auth.ResendConfirmationCodeUseCase
import com.unina.natourkt.core.presentation.base.validation.isCodeValid
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val registrationConfirmationUseCase: RegistrationConfirmationUseCase,
    private val resendConfirmationCodeUseCase: ResendConfirmationCodeUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase
) : ViewModel() {

    /**
     * [ConfirmationUiState] wrapped by StateFlow, used by [ConfirmationFragment]
     */
    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ConfirmationFormUiState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ConfirmationEvent) {
        when (event) {
            is ConfirmationEvent.EnteredCode -> setCode(event.code)
            is ConfirmationEvent.Confirm -> confirmation(event.user)
            is ConfirmationEvent.Resend -> resendCode(event.user)
        }
    }

    fun confirmation(user: String) {
        // On every value emitted by the flow
        if (checkFormValidity()) {
            registrationConfirmationUseCase(
                user,
                _formState.value.code.text
            ).onEach { result ->
                when (result) {
                    // In case of success, update the isConfirmationComplete value
                    is DataState.Success -> {
                        _uiState.value =
                            ConfirmationUiState(isConfirmationComplete = result.data ?: false)

                        analyticsUseCase.sendEvent(ActionEvents.SignedUp)

                        val text = UiText.StringResource(R.string.confirmed_account)
                        _eventFlow.emit(UiEffect.ShowSnackbar(text))
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiState.value =
                            ConfirmationUiState(isLoading = false)

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    is DataState.Loading -> {
                        // In case of loading state, isLoading is true
                        _uiState.value =
                            ConfirmationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun resendCode(user: String) {
        // On every value emitted by the flow
        resendConfirmationCodeUseCase(user).onEach { result ->
            when (result) {
                // In case of success, update the isCoderResent value
                is DataState.Success -> {
                    val text = UiText.StringResource(R.string.code_resent)
                    _eventFlow.emit(UiEffect.ShowSnackbar(text))
                }
                // In case of error, update the error message
                is DataState.Error -> {
                    _uiState.value =
                        ConfirmationUiState(isLoading = false)

                    val errorText = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                }
                // In case of loading state, isLoading is true
                is DataState.Loading -> {
                    _uiState.value =
                        ConfirmationUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setCode(code: String) {
        _formState.update {
            it.copy(code = it.code.copy(text = code))
        }
    }

    private fun checkFormValidity(): Boolean {
        val codeError = formState.value.code.text.isCodeValid()

        _formState.update {
            it.copy(code = it.code.copy(error = codeError))
        }

        return codeError == null
    }
}