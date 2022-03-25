package com.unina.natourkt.feature_profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.auth.LogoutUseCase
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnLogout -> logout()
            SettingsEvent.ThemeChange -> analyticsUseCase.sendEvent(ActionEvents.ThemeChanged)
        }
    }

    private fun logout() {
        logoutUseCase().onEach { result ->
            when (result) {
                is DataState.Success -> {
                    _uiState.value = SettingsUiState(isOperationCompleted = result.data ?: false)
                    analyticsUseCase.sendEvent(ActionEvents.LoggedOut)
                }
                // In case of error, update the error message
                is DataState.Error -> {
                    _uiState.value = SettingsUiState(isLoading = false)
                }
                // In case of loading state, isLoading is true
                is DataState.Loading -> {
                    _uiState.value = SettingsUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}