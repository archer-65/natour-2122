package com.unina.natourkt.presentation.forgot_password.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.ResetPasswordRequestUseCase
import com.unina.natourkt.presentation.base.validation.isUsernameValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    fun resetRequest(username: String) {
        // On every value emitted by the flow
        resetPasswordRequestUseCase(username).onEach { result ->
            when (result) {
                // In case of success, update the isCodeSent value
                is DataState.Success -> {
                    _uiState.value = ForgotPasswordUiState(isCodeSent = result.data ?: false)
                }
                // In case of error, update the error message
                is DataState.Error -> {
                    _uiState.value = ForgotPasswordUiState(errorMessage = result.error)
                }
                // In case of loading state, isLoading is true
                is DataState.Loading -> {
                    _uiState.value = ForgotPasswordUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setUsername(username: String) {
        _formState.update {
            it.copy(username = username, isUsernameValid = username.isUsernameValid())
        }
    }
}