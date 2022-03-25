package com.unina.natourkt.feature_auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.auth.LoginUseCase
import com.unina.natourkt.core.presentation.base.validation.isPasswordValid
import com.unina.natourkt.core.presentation.base.validation.isUsernameValid
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
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
    private val analytics: ActionAnalyticsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormUiState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredUsername -> setUsername(event.username)
            is LoginEvent.EnteredPassword -> setPassword(event.password)
            is LoginEvent.Login -> login()
            is LoginEvent.LoginSocial -> login(event.provider)
        }
    }

    /**
     * Login function
     * @see [LoginUseCase]
     */
    private fun login() {
        // On every value emitted by the flow
        viewModelScope.launch {
            if (checkFormValidity()) {
                loginUseCase(
                    formState.value.username.text,
                    formState.value.password.text
                ).onEach { result ->
                    // Util function
                    resultManager(result)
                }.launchIn(viewModelScope)
            }
        }
    }

    /**
     * Login social function
     * @see [LoginUseCase]
     */
    private fun login(provider: String) {
        // On every value emitted by the flow
        viewModelScope.launch {
            loginUseCase(provider).onEach { result ->
                // Util function
                resultManager(result)
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Manager for same logic in the above functions
     */
    private suspend fun resultManager(result: DataState<Boolean>) {
        when (result) {
            // In case of success, update the isUserLoggedIn value
            is DataState.Success -> {
                _uiState.value = LoginUiState(isUserLoggedIn = result.data ?: false)
                analytics.sendEvent(ActionEvents.LoggedIn)
            }
            // In case of error, update the error message
            is DataState.Error -> {
                _uiState.value = LoginUiState(isLoading = false)

                val errorText = UiTextCauseMapper.mapToText(result.error)
                _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
            }
            // In case of loading state, isLoading is true
            is DataState.Loading -> {
                _uiState.value = LoginUiState(isLoading = true)
            }
        }
    }

    private fun setUsername(username: String) {
        _formState.update {
            it.copy(username = it.username.copy(text = username))
        }
    }

    private fun setPassword(password: String) {
        _formState.update {
            it.copy(password = it.password.copy(text = password))
        }
    }

    private fun checkFormValidity(): Boolean {
        val usernameError = formState.value.username.text.isUsernameValid()
        val passwordError = formState.value.password.text.isPasswordValid()

        _formState.update {
            it.copy(
                username = it.username.copy(error = usernameError),
                password = it.password.copy(error = passwordError)
            )
        }

        return usernameError == null && passwordError == null
    }
}