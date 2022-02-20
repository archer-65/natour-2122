package com.unina.natourkt.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
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
    //private val loginSocialUseCase: LoginSocialUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Login function
     * @see [LoginUseCase]
     */
    fun login(username: String, password: String) {

        viewModelScope.launch {
            // On every value emitted by the flow
            loginUseCase(username, password).onEach { result ->
                // Util function
                resultManager(result)
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Login social function
     * @see [LoginUseCase]
     */
    fun login(provider: String) {

        viewModelScope.launch {
            // On every value emitted by the flow
            loginUseCase(provider).onEach { result ->
                // Util function
                resultManager(result)
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Manager for same logic in the above functions
     */
    private fun resultManager(result: DataState<Boolean>) {
        when (result) {
            // In case of success, update the isUserLoggedIn value
            is DataState.Success -> {
                _uiState.value = LoginUiState(isUserLoggedIn = result.data ?: false)
            }
            // In case of error, update the error message
            is DataState.Error -> {
                _uiState.value = LoginUiState(errorMessage = result.error)
            }
            // In case of loading state, isLoading is true
            is DataState.Loading -> {
                _uiState.value = LoginUiState(isLoading = true)
            }
        }
    }
}