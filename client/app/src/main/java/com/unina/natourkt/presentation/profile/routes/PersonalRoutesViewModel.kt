package com.unina.natourkt.presentation.profile.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import com.unina.natourkt.domain.use_case.route.GetPersonalRoutesUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [PersonalRoutesFragment]
 */
@HiltViewModel
class PersonalRoutesViewModel @Inject constructor(
    private val getPersonalRoutesUseCase: GetPersonalRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    /**
     * [PersonalRoutesUiState] with a set of RouteItemUiState
     */
    // private val _uiState = MutableStateFlow(PersonalRoutesUiState())
    // val uiState: StateFlow<PersonalRoutesUiState> = _uiState.asStateFlow()

    private lateinit var _routesFlow: Flow<PagingData<RouteItemUiState>>
    val routesFlow: Flow<PagingData<RouteItemUiState>>
        get() = _routesFlow

    init {
        getPersonalRoutes()
    }

    fun getPersonalRoutes() {
        viewModelScope.launch {
            _routesFlow = getPersonalRoutesUseCase()
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