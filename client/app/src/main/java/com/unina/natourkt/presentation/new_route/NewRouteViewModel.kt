package com.unina.natourkt.presentation.new_route

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.use_case.maps.GetDirectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewRouteViewModel @Inject constructor(
    private val getDirectionsUseCase: GetDirectionsUseCase
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

    fun getDirections() {
        val stops = uiState.value.routeStops.map { it.toRouteStop() }
        getDirectionsUseCase(stops).onEach { result ->
            when (result) {

                is DataState.Success -> {
                    val polylines = PolylineOptions()
                    result.data?.let { polylines.addAll(it.points) }
                    _uiState.update { it.copy(polylineOptions = polylines) }
                }
                is DataState.Error -> {
                }
                is DataState.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun setPhotos(photos: List<Bitmap>) {
        _uiState.update {
            it.copy(routePhotos = photos)
        }
    }
}

