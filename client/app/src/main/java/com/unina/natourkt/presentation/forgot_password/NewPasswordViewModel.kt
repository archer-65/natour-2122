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
 * ViewModel used by [NewPasswordFragment]
 */
@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val resetPasswordConfirmUseCase: ResetPasswordConfirmUseCase,
) : ViewModel() {

    /**
     * [NewPasswordUiState] wrapped by StateFlow used by [NewPasswordFragment]
     */
    private val _uiState = MutableStateFlow(NewPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun resetConfirm(password: String, code: String) {

        viewModelScope.launch {
            resetPasswordConfirmUseCase(password, code).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.value =
                            NewPasswordUiState(isPasswordReset = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiState.value =
                            NewPasswordUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiState.value = NewPasswordUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}