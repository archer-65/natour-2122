package com.unina.natourkt.presentation.forgot_password.reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.ResetPasswordConfirmUseCase
import com.unina.natourkt.presentation.base.validation.isCodeValid
import com.unina.natourkt.presentation.base.validation.isPasswordValid
import com.unina.natourkt.presentation.registration.RegistrationFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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


    fun resetConfirm() {
        // On every value emitted by the flow
        resetPasswordConfirmUseCase(
            formState.value.password,
            formState.value.code
        ).onEach { result ->
            when (result) {
                // In case of success, update the isPasswordReset value
                is DataState.Success -> {
                    _uiState.value =
                        ResetPasswordUiState(isPasswordReset = result.data ?: false)
                }
                // In case of error, update the error message
                is DataState.Error -> {
                    _uiState.value =
                        ResetPasswordUiState(errorMessage = result.error)
                }
                // In case of loading state, isLoading is true
                is DataState.Loading -> {
                    _uiState.value = ResetPasswordUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setCode(code: String) {
        _formState.update {
            it.copy(code = code, isCodeValid = code.isCodeValid())
        }
    }

    fun setPassword(password: String) {
        _formState.update {
            it.copy(password = password, isPasswordValid = password.isPasswordValid())
        }
    }

    fun setConfirmPassword(confirmPassword: String) {
        _formState.update {
            it.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = confirmPassword.equals(it.password)
            )
        }
    }
}