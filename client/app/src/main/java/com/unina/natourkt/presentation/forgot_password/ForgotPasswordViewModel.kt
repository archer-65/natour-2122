package com.unina.natourkt.presentation.forgot_password

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
            resetPasswordRequestUseCase(username).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.value =
                            ForgotPasswordUiState(isCodeSent = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiState.value =
                            ForgotPasswordUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiState.value = ForgotPasswordUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}