package com.unina.natourkt.presentation.login.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
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
) : ViewModel() {

    /**
     * [ForgotPasswordUiState] wrapped by StateFlow used by [ForgotPasswordFragment]
     */
    private val _uiForgotPasswordState = MutableStateFlow(ForgotPasswordUiState())
    val uiForgotPasswordState = _uiForgotPasswordState.asStateFlow()

    /**
     * [NewPasswordUiState] wrapped by StateFlow used by [NewPasswordFragment]
     */
    private val _uiNewPasswordState = MutableStateFlow(ForgotPasswordUiState())
    val uiNewPasswordState = _uiForgotPasswordState.asStateFlow()

    fun resetRequest(username: String) {

        viewModelScope.launch {
            resetPasswordRequestUseCase(username).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiForgotPasswordState.value = ForgotPasswordUiState(
                            isCodeSent = result.data ?: false,
                            username = username
                        )
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
}