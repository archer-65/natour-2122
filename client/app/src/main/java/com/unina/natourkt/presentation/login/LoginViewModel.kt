package com.unina.natourkt.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.auth.LoginSocialUseCase
import com.unina.natourkt.domain.use_case.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [LoginFragment]
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginSocialUseCase: LoginSocialUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Login function /w Flows
     * @see [LoginUseCase]
     */
    fun login(username: String, password: String) {

        viewModelScope.launch {
            loginUseCase(username, password).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.value = LoginUiState(isUserLoggedIn = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiState.value = LoginUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiState.value = LoginUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Login social function /w Flows
     * @see [LoginSocialUseCase]
     */
    fun login(provider: String) {

        viewModelScope.launch {
            loginSocialUseCase(provider).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.value = LoginUiState(isUserLoggedIn = result.data ?: false)
                    }
                    is DataState.Error -> {
                        _uiState.value = LoginUiState(errorMessage = result.error)
                    }
                    is DataState.Loading -> {
                        _uiState.value = LoginUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}