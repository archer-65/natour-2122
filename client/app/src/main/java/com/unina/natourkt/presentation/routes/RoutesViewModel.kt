package com.unina.natourkt.presentation.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.route.GetRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val getRoutesUseCase: GetRoutesUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    init {
        getRoutes()
    }

    fun getRoutes() {

        viewModelScope.launch {
            val routes = getRoutesUseCase()
                .cachedIn(viewModelScope)
                .first()
                .map { it.toUi() }

            _uiState.update {
                it.copy(routeItems = routes)
            }
        }
    }
}