package com.unina.natourkt.feature_compilation.delete_compilation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.domain.use_case.compilation.DeleteCompilationUseCase
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DeleteCompilationViewModel @Inject constructor(
    private val deleteCompilationUseCase: DeleteCompilationUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    private val compilationId = savedState.get<Long>("compilationToDeleteId")

    private val _uiState = MutableStateFlow(DeleteCompilationUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun removeCompilation() {
        deleteCompilationUseCase(compilationId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val text = UiText.StringResource(R.string.compilation_deleted)
                    _eventFlow.emit(UiEvent.ShowToast(text))

                    _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                }
                is DataState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is DataState.Error -> {
                    _uiState.update { it.copy(isLoading = false) }

                    val text = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEvent.ShowToast(text))
                }
            }
        }.launchIn(viewModelScope)
    }
}