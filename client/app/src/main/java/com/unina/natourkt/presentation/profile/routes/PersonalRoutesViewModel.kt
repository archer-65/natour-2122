package com.unina.natourkt.presentation.profile.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import com.unina.natourkt.domain.use_case.route.GetPersonalRoutesUseCase
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
) : ViewModel() {

    /**
     * [PersonalRoutesUiState] with a set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(PersonalRoutesUiState())
    val uiState: StateFlow<PersonalRoutesUiState> = _uiState.asStateFlow()

    init {
        getPersonalRoutes()
    }

    fun getPersonalRoutes() {

        viewModelScope.launch {

            // Get routes and map to ItemUiState
            val routes = getPersonalRoutesUseCase.invoke()
                .cachedIn(viewModelScope)
                .first()
                .map { it.toUi() }

            _uiState.update {
                it.copy(routeItems = routes)
            }
        }
    }
}