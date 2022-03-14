package com.unina.natourkt.feature_profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val uiMapper: UserUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLoggedUser()
    }

    private fun getLoggedUser() {
        viewModelScope.launch {
            _uiState.update {
                val userUi = uiMapper.mapToUi(getUserDataUseCase()!!)
                it.copy(loggedUser = userUi)
            }
        }
    }
}