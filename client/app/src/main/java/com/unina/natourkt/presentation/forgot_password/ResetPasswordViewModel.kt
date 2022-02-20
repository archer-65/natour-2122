package com.unina.natourkt.presentation.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.ResetPasswordConfirmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    fun resetConfirm(password: String, code: String) {

        viewModelScope.launch {

            // On every value emitted by the flow
            resetPasswordConfirmUseCase(password, code).onEach { result ->
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
    }
}