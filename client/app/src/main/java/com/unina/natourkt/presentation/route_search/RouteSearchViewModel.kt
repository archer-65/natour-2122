package com.unina.natourkt.presentation.route_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.use_case.route.GetFilteredRoutesUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RouteSearchViewModel @Inject constructor(
    private val getFilteredRoutesUseCase: GetFilteredRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteSearchUiState())

//    private lateinit var _routeResultsFlow: Flow<PagingData<RouteItemUiState>>
//    val routeResultsFlow: Flow<PagingData<RouteItemUiState>>
//        get() = _routeResultsFlow

    fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    val routeResultsFlow =
        _uiState.flatMapLatest { filter ->
            getFilteredRoutesUseCase(filter.toFilter())
                .map { pagingData ->
                    pagingData.map { route ->
                        route.toUi().convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }.cachedIn(viewModelScope)
        }
}