package com.unina.natourkt.feature_compilation.save_into_compilation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.domain.use_case.compilation.AddCompilationRouteUseCase
import com.unina.natourkt.core.domain.use_case.compilation.GetPersonalCompilationsToAddRoute
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.mapper.CompilationDialogItemUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveIntoCompilationViewModel @Inject constructor(
    private val getPersonalCompilationsToAddRoute: GetPersonalCompilationsToAddRoute,
    private val addCompilationRouteUseCase: AddCompilationRouteUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val compilationDialogItemUiMapper: CompilationDialogItemUiMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val routeId = savedStateHandle.get<Long>("routeToSaveId")
    private val userId = savedStateHandle.get<Long>("ownerCompilationId")

    private val _uiState = MutableStateFlow(SaveIntoCompilationUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getCompilationsToSave()
    }

    fun onEvent(event: SaveIntoCompilationEvent) {
        when (event) {
            is SaveIntoCompilationEvent.OnSave -> saveRouteIntoCompilation(event.compilationId)
        }
    }

    private fun getCompilationsToSave() {
        viewModelScope.launch {
            getPersonalCompilationsToAddRoute(userId!!, routeId!!).onEach { result ->
                when (result) {
                    is DataState.Success -> _uiState.update {
                        val compilations =
                            result.data?.map {
                                compilationDialogItemUiMapper.mapToUi(it).convertKeys {
                                    getUrlFromKeyUseCase(it)
                                }
                            } ?: emptyList()

                        it.copy(compilations = compilations)
                    }
                    is DataState.Error -> {}
                    is DataState.Loading -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun saveRouteIntoCompilation(compilationId: Long) {
        addCompilationRouteUseCase(compilationId, routeId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    _eventFlow.emit(UiEffect.ShowToast(UiText.StringResource(R.string.route_added_compilation)))
                    _eventFlow.emit(UiEffect.DismissDialog)
                }
                is DataState.Error -> {
                    val errorText = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowToast((errorText)))
                }
                is DataState.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}