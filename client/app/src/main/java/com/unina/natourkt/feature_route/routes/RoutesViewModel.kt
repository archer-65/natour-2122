package com.unina.natourkt.feature_route.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.route.GetRoutesUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.RouteItemUiMapper
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
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
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase,
    private val userUiMapper: UserUiMapper,
    private val routeItemUiMapper: RouteItemUiMapper,
) : ViewModel() {

    /**
     * [RoutesUiState] with set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(RoutesUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUi>>
    val routesFlow: Flow<PagingData<RouteItemUi>>
        get() = _routesFlow

    init {
        getRoutes()
        getUser()
    }

    fun onEvent(event: RoutesEvent) {
        when (event) {
            RoutesEvent.ClickRoute -> analyticsUseCase.sendEvent(ActionEvents.ClickRoute)
        }
    }

    private fun getRoutes() {
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

    private fun getUser() {
        viewModelScope.launch {
            _uiState.update {
                val user = getUserDataUseCase()?.let { userUiMapper.mapToUi(it) }
                it.copy(loggedUser = user)
            }
        }
    }
}