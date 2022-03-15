package com.unina.natourkt.feature_profile.profile.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.domain.use_case.route.GetPersonalRoutesUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.RouteItemUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [ProfileRoutesFragment]
 */
@HiltViewModel
class ProfileRoutesViewModel @Inject constructor(
    private val getPersonalRoutesUseCase: GetPersonalRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val routeItemUiMapper: RouteItemUiMapper,
) : ViewModel() {

    /**
     * [ProfileRoutesUiState] with a set of RouteItemUiState
     */
    // private val _uiState = MutableStateFlow(PersonalRoutesUiState())
    // val uiState: StateFlow<PersonalRoutesUiState> = _uiState.asStateFlow()

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUi>>
    val routesFlow: Flow<PagingData<RouteItemUi>>
        get() = _routesFlow

    init {
        getPersonalRoutes()
    }

    fun getPersonalRoutes() {
        viewModelScope.launch {
            _routesFlow = getPersonalRoutesUseCase()
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