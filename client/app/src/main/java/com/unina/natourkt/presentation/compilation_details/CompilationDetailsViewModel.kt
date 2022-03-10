package com.unina.natourkt.presentation.compilation_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.use_case.route.GetCompilationRoutesUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompilationDetailsViewModel @Inject constructor(
    private val getCompilationRoutesUseCase: GetCompilationRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    savedState: SavedStateHandle,
) : ViewModel() {

    val compilation = savedState.get<CompilationItemUiState>("compilationItem")

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUiState>>
    val routesFlow: Flow<PagingData<RouteItemUiState>>
        get() = _routesFlow

    init {
        getCompilationRoutes()
    }

    fun getCompilationRoutes() {
        viewModelScope.launch {
            _routesFlow = getCompilationRoutesUseCase(compilation!!.id)
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