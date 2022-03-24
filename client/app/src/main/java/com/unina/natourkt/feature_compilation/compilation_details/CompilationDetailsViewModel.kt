package com.unina.natourkt.feature_compilation.compilation_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.R
import com.unina.natourkt.core.domain.use_case.compilation.RemoveCompilationRouteUseCase
import com.unina.natourkt.core.domain.use_case.route.GetCompilationRoutesUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.RouteItemUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompilationDetailsViewModel @Inject constructor(
    private val getCompilationRoutesUseCase: GetCompilationRoutesUseCase,
    private val removeCompilationRouteUseCase: RemoveCompilationRouteUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val routeItemUiMapper: RouteItemUiMapper,
    savedState: SavedStateHandle,
) : ViewModel() {

    val compilation = savedState.get<CompilationItemUi>("compilationItem")

    private val _uiState = MutableStateFlow(CompilationDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUi>>
    val routesFlow: Flow<PagingData<RouteItemUi>>
        get() = _routesFlow

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getCompilationRoutes()
    }

    fun onEvent(event: CompilationDetailsEvent) {
        when (event) {
            is CompilationDetailsEvent.OnDeleteRouteFromCompilation -> deleteRouteFromCompilation(
                event.routeId,
                event.position
            )
        }
    }

    private fun getCompilationRoutes() {
        viewModelScope.launch {
            _routesFlow = getCompilationRoutesUseCase(compilation!!.id)
                .map { pagingData ->
                    pagingData.map { route ->
                        val routeUi = routeItemUiMapper.mapToUi(route)
                        routeUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }

    private fun deleteRouteFromCompilation(routeId: Long, position: Int) {
        removeCompilationRouteUseCase(compilation!!.id, routeId).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRemoved = true,
                            removedPosition = position
                        )
                    }

                    getCompilationRoutes()

                    val text = UiText.StringResource(R.string.route_removed_from_compilation)
                    _eventFlow.emit(UiEffect.ShowSnackbar(text))
                }
                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isRemoved = false,
                            removedPosition = null
                        )
                    }
                }
                is DataState.Error -> {
                    _uiState.update { it.copy(isLoading = false, removedPosition = null) }

                    val text = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowSnackbar(text))
                }
            }
        }.launchIn(viewModelScope)
    }
}