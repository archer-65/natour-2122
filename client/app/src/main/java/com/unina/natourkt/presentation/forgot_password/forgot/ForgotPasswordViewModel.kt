package com.unina.natourkt.presentation.forgot_password.forgot

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

    fun resetRequest(username: String) {

        viewModelScope.launch {

            // On every value emitted by the flow
            resetPasswordRequestUseCase(username).onEach { result ->
                when (result) {
                    // In case of success, update the isCodeSent value
                    is DataState.Success -> {
                        _uiState.value =
                            ForgotPasswordUiState(isCodeSent = result.data ?: false)
                    }
                    // In case of error, update the error message
                    is DataState.Error -> {
                        _uiState.value =
                            ForgotPasswordUiState(errorMessage = result.error)
                    }
                    // In case of loading state, isLoading is true
                    is DataState.Loading -> {
                        _uiState.value = ForgotPasswordUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}