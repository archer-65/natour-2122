package com.unina.natourkt.feature_compilation.create_compilation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.compilation.CreateCompilationUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCompilationViewModel @Inject constructor(
    private val createCompilationUseCase: CreateCompilationUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCompilationUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreateCompilationEvent) {
        when (event) {
            is CreateCompilationEvent.EnteredTitle -> setTitle(event.title)
            is CreateCompilationEvent.EnteredDescription -> setDescription(event.description)
            is CreateCompilationEvent.InsertedPhoto -> setPhoto(event.photo)
            is CreateCompilationEvent.Upload -> uploadCompilation()
        }
    }


    private fun setTitle(title: String) {
        _uiState.update {
            it.copy(compilationTitle = it.compilationTitle.copy(text = title))
        }
    }

    private fun setDescription(description: String) {
        _uiState.update {
            it.copy(compilationDescription = it.compilationDescription.copy(text = description))
        }
    }

    private fun setPhoto(photo: Uri) {
        _uiState.update {
            it.copy(compilationPhoto = photo)
        }
    }

    private fun uploadCompilation() {
        viewModelScope.launch {
            createCompilationUseCase(mapForCreation()).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.update {
                            it.copy(inInserted = true, isLoading = false)
                        }

                        analyticsUseCase.sendEvent(ActionEvents.CreateCompilation)

                        val text = UiText.StringResource(R.string.compilation_created)
                        _eventFlow.emit(UiEffect.ShowSnackbar(text))
                    }
                    is DataState.Loading -> _uiState.update {
                        it.copy(isLoading = true)
                    }
                    is DataState.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun mapForCreation(): CompilationCreation {
        return CompilationCreation(
            title = uiState.value.compilationTitle.text,
            description = uiState.value.compilationDescription.text,
            photo = uiState.value.compilationPhoto.toString(),
            author = getUserDataUseCase(),
        )
    }
}