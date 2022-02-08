package com.unina.natourkt.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.usecase.ConfirmationUseCase
import com.unina.natourkt.domain.usecase.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val confirmationUseCase: ConfirmationUseCase
) : ViewModel() {

    private val _uiRegistrationState = MutableStateFlow(RegistrationUiState())
    val uiRegistrationState: StateFlow<RegistrationUiState> = _uiRegistrationState.asStateFlow()

    private val _uiConfirmationState = MutableStateFlow(ConfirmationUiState())
    val uiConfirmationState: StateFlow<ConfirmationUiState> = _uiConfirmationState.asStateFlow()

    fun registration(username: String, email: String, password: String) {

        viewModelScope.launch {
            registrationUseCase(username, email, password).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiRegistrationState.value =
                            RegistrationUiState(
                                isSignUpComplete = result.data ?: false,
                                username = username
                            )
                    }
                    is DataState.Error -> {
                        _uiRegistrationState.value =
                            RegistrationUiState(errorMessage = result.message)
                    }
                    is DataState.Loading -> {
                        _uiRegistrationState.value = RegistrationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun confirmation(code: String) {

        viewModelScope.launch {
            confirmationUseCase(uiRegistrationState.value.username!!, code).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isConfirmationComplete = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(errorMessage = result.message)
                    }
                    is DataState.Loading -> {
                        _uiConfirmationState.value =
                            ConfirmationUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}