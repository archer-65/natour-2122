package com.unina.natourkt.presentation.new_route

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewRouteViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRouteUiState())
    val uiState: StateFlow<NewRouteUiState> = _uiState.asStateFlow()

    fun setInfo(routeInfo: NewRouteInfo) {
        _uiState.update { currentState ->
            currentState.copy(routeInfo = routeInfo)
        }
    }

    fun addStop(latitude: Double, longitude: Double) {
        _uiState.update { currentState ->
            val newStops = currentState.routeStops + NewRouteStop(
                stopNumber = currentState.routeStops.size + 1,
                latitude,
                longitude
            )
            currentState.copy(routeStops = newStops)
        }
    }
}

