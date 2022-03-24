package com.unina.natourkt.feature_auth.forgot_password.reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.auth.ResetPasswordConfirmUseCase
import com.unina.natourkt.core.presentation.base.validation.*
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel used by [ResetPasswordFragment]
 */
@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordConfirmUseCase: ResetPasswordConfirmUseCase,
) : ViewModel() {

    /**
     * [ResetPasswordUiState] wrapped by StateFlow used by [ResetPasswordFragment]
     */
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ResetPasswordFormUiState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.EnteredCode -> setCode(event.code)
            is ResetPasswordEvent.EnteredPassword -> setPassword(event.password)
            is ResetPasswordEvent.EnteredConfirmPassword -> setConfirmPassword(event.confirmPassword)
            is ResetPasswordEvent.Reset -> resetConfirm()
        }
    }


    private fun resetConfirm() {
        // On every value emitted by the flow
        if (checkFormValidity()) {
            resetPasswordConfirmUseCase(
                formState.value.password.text,
                formState.value.code.text
            ).onEach { result ->
                when (result) {
                    // In case of success, update the isPasswordReset value
                    is DataState.Success -> {
                        _uiState.value =
                            ResetPasswordUiState(isPasswordReset = result.data ?: false)

                        val text = UiText.StringResource(R.string.password_reset_success)
                        _eventFlow.emit(UiEffect.ShowSnackbar(text))
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiState.value = ResetPasswordUiState(isLoading = false)

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    // In case of loading state, isLoading is true
                    is DataState.Loading -> {
                        _uiState.value = ResetPasswordUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setCode(code: String) {
        _formState.update {
            it.copy(code = it.code.copy(text = code))
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
        val codeError = formState.value.code.text.isUsernameValid()

        val passwordError = formState.value.password.text.isPasswordValid()

        val confirmPasswordError =
            formState.value.confirmPassword.text.equalsOtherString(_formState.value.password.text)

        _formState.update {
            it.copy(
                code = it.code.copy(error = codeError),
                password = it.password.copy(error = passwordError),
                confirmPassword = it.password.copy(error = confirmPasswordError)
            )
        }

        return codeError == null && passwordError == null && confirmPasswordError == null
    }
}