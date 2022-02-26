package com.unina.natourkt.presentation.new_route

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class NewRouteViewModel: ViewModel() {


    private val _uiState = MutableStateFlow(NewRouteUiState())

    val uiState: StateFlow<NewRouteUiState> = _uiState.asStateFlow()

    fun setInfo(routeInfo: NewRoute) {

        _uiState.update {
            it.copy(route = routeInfo)
        }

    }
}

