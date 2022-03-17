package com.unina.natourkt.feature_route.routes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.R
import com.unina.natourkt.core.domain.use_case.compilation.AddCompilationRouteUseCase
import com.unina.natourkt.core.domain.use_case.compilation.GetPersonalCompilationsToAddRoute
import com.unina.natourkt.core.domain.use_case.route.GetRoutesUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.CompilationDialogItemUiMapper
import com.unina.natourkt.core.presentation.model.mapper.RouteItemUiMapper
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [RoutesFragment]
 */
@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val getRoutesUseCase: GetRoutesUseCase,
    private val getPersonalCompilationsToAddRoute: GetPersonalCompilationsToAddRoute,
    private val addCompilationRouteUseCase: AddCompilationRouteUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val routeItemUiMapper: RouteItemUiMapper,
    private val compilationDialogItemUiMapper: CompilationDialogItemUiMapper,
) : ViewModel() {

    /**
     * [RoutesUiState] with set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(RoutesUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUi>>
    val routesFlow: Flow<PagingData<RouteItemUi>>
        get() = _routesFlow

    init {
        getRoutes()
    }

    fun getRoutes() {
        viewModelScope.launch {
            _routesFlow = getRoutesUseCase()
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

    fun getCompilationsToSave(routeId: Long) {
        viewModelScope.launch {
            getUserDataUseCase()?.id?.let {
                getPersonalCompilationsToAddRoute(it, routeId).onEach { result ->
                    when (result) {
                        is DataState.Success -> _uiState.update {
                            val compilations =
                                result.data?.map {
                                    compilationDialogItemUiMapper.mapToUi(it).convertKeys {
                                        getUrlFromKeyUseCase(it)
                                    }
                                } ?: emptyList()

                            it.copy(compilations = compilations, selectedRoute = routeId)
                        }
                        is DataState.Error -> {}
                        is DataState.Loading -> {}
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun saveRouteIntoCompilation(compilationId: Long) {
        uiState.value.selectedRoute?.let {
            addCompilationRouteUseCase(compilationId, it).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        _eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(R.string.route_added_compilation)))
                        _eventFlow.emit(UiEvent.DismissDialog)
                    }
                    is DataState.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEvent.ShowSnackbar((errorText)))
                    }
                    is DataState.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}