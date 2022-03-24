package com.unina.natourkt.feature_profile.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.domain.use_case.user.UpdateUserPhotoUseCase
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val uiMapper: UserUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLoggedUser()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnUpdatePhoto -> updatePhoto(event.uri)
        }
    }

    private fun getLoggedUser() {
        viewModelScope.launch {
            _uiState.update {
                val userUi = uiMapper.mapToUi(getUserDataUseCase()!!).convertKeys {
                    getUrlFromKeyUseCase(it)
                }
                it.copy(loggedUser = userUi)
            }
        }
    }

    private fun updatePhoto(uri: Uri) {
        val user = uiState.value.loggedUser?.copy(photo = uri.toString())
        user?.let {
            updateUserPhotoUseCase(uiMapper.mapToDomain(it)).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        val userUi = result.data?.let {
                            uiMapper.mapToUi(it).convertKeys {
                                getUrlFromKeyUseCase(it)
                            }
                        }

                        _uiState.update { it.copy(loggedUser = userUi, isPhotoUpdated = true) }
                    }
                    is DataState.Loading -> {
                        _uiState.update { it.copy(isLoading = true, isPhotoUpdated = false) }
                    }
                    is DataState.Error -> {
                        _uiState.update { it.copy(isLoading = false, isPhotoUpdated = false) }

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}