package com.unina.natourkt.presentation.login.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.ResetPasswordConfirmUseCase
import com.unina.natourkt.domain.use_case.auth.ResetPasswordRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SharedViewModel used by [ForgotPasswordFragment] and [NewPasswordFragment]
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordRequestUseCase: ResetPasswordRequestUseCase,
    private val resetPasswordConfirmUseCase: ResetPasswordConfirmUseCase,
) : ViewModel() {

    /**
     * [ForgotPasswordUiState] wrapped by StateFlow used by [ForgotPasswordFragment]
     */
    private val _uiForgotPasswordState = MutableStateFlow(ForgotPasswordUiState())
    val uiForgotPasswordState = _uiForgotPasswordState.asStateFlow()

    /**
     * [NewPasswordUiState] wrapped by StateFlow used by [NewPasswordFragment]
     */
    private val _uiNewPasswordState = MutableStateFlow(NewPasswordUiState())
    val uiNewPasswordState = _uiNewPasswordState.asStateFlow()

    fun resetRequest(username: String) {

        viewModelScope.launch {
            resetPasswordRequestUseCase(username).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiForgotPasswordState.value =
                            ForgotPasswordUiState(isCodeSent = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiForgotPasswordState.value =
                            ForgotPasswordUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiForgotPasswordState.value = ForgotPasswordUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun resetConfirm(password: String, code: String) {

        viewModelScope.launch {
            resetPasswordConfirmUseCase(password, code).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiNewPasswordState.value =
                            NewPasswordUiState(isPasswordReset = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiNewPasswordState.value =
                            NewPasswordUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiNewPasswordState.value = NewPasswordUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}