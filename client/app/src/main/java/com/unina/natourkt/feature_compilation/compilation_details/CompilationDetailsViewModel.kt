package com.unina.natourkt.feature_compilation.compilation_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.domain.use_case.route.GetCompilationRoutesUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.RouteItemUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompilationDetailsViewModel @Inject constructor(
    private val getCompilationRoutesUseCase: GetCompilationRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val routeItemUiMapper: RouteItemUiMapper,
    savedState: SavedStateHandle,
) : ViewModel() {

    val compilation = savedState.get<CompilationItemUi>("compilationItem")

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUi>>
    val routesFlow: Flow<PagingData<RouteItemUi>>
        get() = _routesFlow

    init {
        getCompilationRoutes()
    }

    fun getCompilationRoutes() {
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
}