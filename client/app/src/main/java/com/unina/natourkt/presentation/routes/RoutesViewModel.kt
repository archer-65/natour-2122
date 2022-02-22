package com.unina.natourkt.presentation.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.route.GetRoutesUseCase
import com.unina.natourkt.presentation.home.HomeFragment
import com.unina.natourkt.presentation.home.HomeUiState
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
): ViewModel() {

    /**
     * [RouteUiState] with set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    init {
        getRoutes()
    }

    fun getRoutes() {

        viewModelScope.launch {

            // Get routes and map to ItemUiState
            val routes = getRoutesUseCase()
                .cachedIn(viewModelScope)
                .first()
                .map { it.toUi() }

            // Update the General UiState
            _uiState.update {
                it.copy(routeItems = routes)
            }
        }
    }
}