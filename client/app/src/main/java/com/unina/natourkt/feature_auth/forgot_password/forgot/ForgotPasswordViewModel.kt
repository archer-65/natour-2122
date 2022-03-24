package com.unina.natourkt.feature_auth.forgot_password.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.auth.ResetPasswordRequestUseCase
import com.unina.natourkt.core.presentation.base.validation.isUsernameValid
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel used by [ForgotPasswordFragment]
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordRequestUseCase: ResetPasswordRequestUseCase,
) : ViewModel() {

    /**
     * [ForgotPasswordUiState] wrapped by StateFlow used by [ForgotPasswordFragment]
     */
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ForgotPasswordFormUiState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EnteredUsername -> setUsername(event.username)
            is ForgotPasswordEvent.Reset -> resetRequest()
        }
    }

    private fun resetRequest() {
        // On every value emitted by the flow
        if (checkFormValidity()) {
            resetPasswordRequestUseCase(formState.value.username.text).onEach { result ->
                when (result) {
                    // In case of success, update the isCodeSent value
                    is DataState.Success -> {
                        _uiState.value = ForgotPasswordUiState(isCodeSent = result.data ?: false)

                        val text = UiText.StringResource(R.string.code_resent_password)
                        _eventFlow.emit(UiEffect.ShowSnackbar(text))
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiState.value = ForgotPasswordUiState(isLoading = false)

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    // In case of loading state, isLoading is true
                    is DataState.Loading -> {
                        _uiState.value = ForgotPasswordUiState(isLoading = true)
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

    private fun checkFormValidity(): Boolean {
        val usernameError = formState.value.username.text.isUsernameValid()

        _formState.update {
            it.copy(
                username = it.username.copy(error = usernameError),
            )
        }

        return usernameError == null
    }
}