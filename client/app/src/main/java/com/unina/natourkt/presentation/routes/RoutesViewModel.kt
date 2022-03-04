package com.unina.natourkt.presentation.routes

import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.route.GetRoutesUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
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
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    /**
     * [RouteUiState] with set of RouteItemUiState
     */
    // private val _uiState = MutableStateFlow(RouteUiState())
    // val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUiState>>
    val routesFlow: Flow<PagingData<RouteItemUiState>>
        get() = _routesFlow

    init {
        getRoutes()
    }

    fun getRoutes() {
        viewModelScope.launch {
            _routesFlow = getRoutesUseCase()
                .map { pagingData ->
                    pagingData.map { route ->
                        route.toUi().convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}