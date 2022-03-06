package com.unina.natourkt.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.settings.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLoggedUser()
    }

    private fun getLoggedUser() {
        viewModelScope.launch {
            _uiState.update {
                val user = getUserDataUseCase()
                it.copy(loggedUser = user?.toUi())
            }
        }
    }
}